package io.gamov.hackers.passwords;

import com.google.common.base.Strings;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.Produced;

import java.util.HashSet;
import java.util.Properties;

import io.gamov.hackers.passwords.util.MySerdes;
import io.gamov.hackers.passwords.util.Parser;
import lombok.extern.slf4j.Slf4j;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;

@Slf4j
public class App {

  private static final String APPLICATION_ID = "kafka-leaked-passwords";

  public static void main(String[] args) {

    String kafkaBootstrapServerFromEnv = System.getenv("KAFKA_BOOTSTRAP_SERVERS");
    // String kafkaBootstapServerFromCmd = args[0];
    String bootstrapServers =
        Strings.isNullOrEmpty(kafkaBootstrapServerFromEnv) ? "localhost:9092"
                                                           : kafkaBootstrapServerFromEnv;

    log.info("Bootstrap servers: {}", bootstrapServers);

    Properties streamsConfiguration = new Properties();
    streamsConfiguration.put(APPLICATION_ID_CONFIG, APPLICATION_ID);

    streamsConfiguration.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    streamsConfiguration
        .put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    streamsConfiguration
        .put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    streamsConfiguration.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
    streamsConfiguration.put(CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);

    StreamsBuilder builder = new StreamsBuilder();

    builder.stream("BINTRAY_STREAM",
                   Consumed.with(Serdes.String(),
                                 Serdes.String()))
        // parsing json string from source stream
        .mapValues(Parser::parse)
        // we're only interested in successful logins
        .filter((key, value) -> "login_success".equals(value.getType()))
        // re-keying with `subject` as key
        .selectKey((key, value) -> value.getSubject())
        // grouping by `subject`
        .groupByKey()
        .aggregate(
            (Initializer<HashSet<String>>) HashSet::new,
            (key, value, aggregate) -> {
              String ipAddress = value.getIpAddress();
              aggregate.add(ipAddress);
              return aggregate;
            }
        )
        // persist only if login happened from more than 1 distinct IP
        .filter((key, value) -> value.size() > 1)
        .toStream()
        .to("BINTRAY_LEAKED_LOGINS",
            Produced.with(Serdes.String(),
                          new MySerdes.SetOfIpsSerde()));

    KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfiguration);
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    streams.start();

  }
}
