package io.gamov.hackers.passwords.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * transformed by KSQL JSON
 *
 * {
 *   "ROWTIME": 1526667213579,
 *   "ROWKEY": "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"string\",\"optional\":false,\"field\":\"Id\"}],\"optional\":false,\"name\":\"io.gamov.bintray.model.FirehoseEvent.EventId\",\"doc\":\"Key for a firehose event.\"},\"payload\":{\"Id\":\"50.232.40.61\"}}",
 *   "PATH": "/russian-hackers/hacking-utils/public/semaphore-bank-hack-1.0.tar",
 *   "MYTYPE": "download",
 *   "USER_AGENT": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.1 Safari/605.1.15",
 *   "IP": "50.232.40.61",
 *   "SUBJECT": "gamussa",
 *   "TIME": "2018-05-18T18:13:33.136Z"
 * }
 *
 */
public class FirehoseEvent {

  @JsonProperty("MYTYPE")
  private String type;

  @JsonProperty("PATH")
  private String path;

  @JsonProperty("USER_AGENT")
  private String userAgent;

  @JsonProperty("content_length")
  private int contentLength;

  @Override
  public String toString() {
    return "FirehoseEvent{" + "type='" + type + '\''
           + ", path='" + path + '\''
           + ", userAgent='" + userAgent + '\''
           + ", contentLength=" + contentLength
           + ", ipAddress='" + ipAddress + '\''
           + ", subject='" + subject + '\''
           + ", time='" + time + '\''
           + '}';
  }

  @JsonProperty("IP")
  private String ipAddress;

  @JsonProperty("SUBJECT")
  private String subject;

  @JsonProperty("TIME")
  private String time;

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public int getContentLength() {
    return contentLength;
  }

  public void setContentLength(int contentLength) {
    this.contentLength = contentLength;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }
}
