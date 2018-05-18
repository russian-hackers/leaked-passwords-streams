package io.gamov.hackers.passwords.util;

import com.google.gson.reflect.TypeToken;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.gamov.hackers.passwords.model.FirehoseEvent;

public class MySerdes {

  public static final class FirehoseEventSerde extends WrapperSerde<FirehoseEvent> {

    public FirehoseEventSerde() {
      super(new JsonSerializer<>(), new JsonDeserializer<>(FirehoseEvent.class));
    }
  }

  public static final class SetOfIpsSerde extends WrapperSerde<HashSet<String>> {

    private static final Type listType = new TypeToken<Set<String>>() {
    }.getType();

    public SetOfIpsSerde() {
      super(new JsonSerializer<>(), new JsonDeserializer<>(listType));
    }
  }

  private static class WrapperSerde<T> implements Serde<T> {

    private JsonSerializer<T> serializer;
    private JsonDeserializer<T> deserializer;

    WrapperSerde(JsonSerializer<T> serializer, JsonDeserializer<T> deserializer) {
      this.serializer = serializer;
      this.deserializer = deserializer;
    }

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public void close() {

    }

    @Override
    public Serializer<T> serializer() {
      return serializer;
    }

    @Override
    public Deserializer<T> deserializer() {
      return deserializer;
    }
  }

}
