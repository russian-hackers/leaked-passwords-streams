package io.gamov.hackers.passwords.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import io.gamov.hackers.passwords.model.FirehoseEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Parser {

  private static ObjectMapper objectMapper = new ObjectMapper();

  public static FirehoseEvent parse(String text) {
    FirehoseEvent firehoseEvent = new FirehoseEvent();
    objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
    try {
      firehoseEvent = objectMapper.readValue(text, FirehoseEvent.class);
    } catch (IOException e) {
      log.error("Can't parse " + text, e);
    }
    return firehoseEvent;
  }

}
