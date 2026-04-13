package com.thedummyway.imperative.ai.tools;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "ai.model")
public interface ChatModelConfiguration {

  String name();

  double temperature();

  double topP();

  int topK();

  int timeout();

  boolean logResponses();

  boolean logRequests();

  boolean think();

  boolean returnThinking();

  String baseUrl();
  
  int maxRetries();
}
