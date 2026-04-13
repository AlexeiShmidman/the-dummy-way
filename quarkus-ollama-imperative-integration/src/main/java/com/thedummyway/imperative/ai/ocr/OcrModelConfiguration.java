package com.thedummyway.imperative.ai.ocr;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "ai.ocr")
public interface OcrModelConfiguration {

  String name();

  double temperature();

  int timeout();

  boolean logResponses();

  boolean logRequests();

  boolean think();

  String baseUrl();

  int maxRetries();
}
