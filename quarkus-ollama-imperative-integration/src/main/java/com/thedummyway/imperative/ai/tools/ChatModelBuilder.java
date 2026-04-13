package com.thedummyway.imperative.ai.tools;

import java.time.Duration;

import dev.langchain4j.model.ollama.OllamaChatModel;
import jakarta.inject.Singleton;

@Singleton
public class ChatModelBuilder {

  private final ChatModelConfiguration modelConfiguration;

  private String modelName;
  private String baseUrl;
  private Boolean think;
  private Integer maxRetries;

  private Duration timeout;
  private Boolean logRequests;
  private Boolean logResponses;
  private Double temperature;
  private Double topP;
  private Integer topK;
  private Boolean returnThinking;

  public ChatModelBuilder(ChatModelConfiguration modelConfiguration) {
    this.modelConfiguration = modelConfiguration;
  }

  public ChatModelBuilder modelName(String modelName) {
    this.modelName = modelName;
    return this;
  }

  public ChatModelBuilder baseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
    return this;
  }

  public ChatModelBuilder think(Boolean think) {
    this.think = think;
    return this;
  }

  public ChatModelBuilder maxRetries(Integer maxRetries) {
    this.maxRetries = maxRetries;
    return this;
  }

  public ChatModelBuilder timeout(Duration timeout) {
    this.timeout = timeout;
    return this;
  }

  public ChatModelBuilder logRequests(Boolean logRequests) {
    this.logRequests = logRequests;
    return this;
  }

  public ChatModelBuilder logResponses(Boolean logResponses) {
    this.logResponses = logResponses;
    return this;
  }

  public ChatModelBuilder temperature(Double temperature) {
    this.temperature = temperature;
    return this;
  }

  public ChatModelBuilder returnThinking(boolean returnThinking) {
    this.returnThinking = returnThinking;
    return this;
  }

  public ChatModelBuilder topP(double topP) {
    this.topP = topP;
    return this;
  }

  public ChatModelBuilder topK(int topK) {
    this.topK = topK;
    return this;
  }

  public OllamaChatModel build() {
    return OllamaChatModel.builder()
        .modelName(getOrDefault(modelName, modelConfiguration.name()))
        .baseUrl(getOrDefault(baseUrl, modelConfiguration.baseUrl()))
        .think(getOrDefault(think, modelConfiguration.think()))
        .maxRetries(getOrDefault(maxRetries, modelConfiguration.maxRetries()))
        .timeout(getOrDefault(timeout, Duration.ofSeconds(modelConfiguration.timeout())))
        .logRequests(getOrDefault(logRequests, modelConfiguration.logRequests()))
        .logResponses(getOrDefault(logResponses, modelConfiguration.logResponses()))
        .temperature(getOrDefault(temperature, modelConfiguration.temperature()))
        .returnThinking(getOrDefault(returnThinking, modelConfiguration.returnThinking()))
        .topP(getOrDefault(topP, modelConfiguration.topP()))
        .topK(getOrDefault(topK, modelConfiguration.topK()))
        .build();
  }

  public OllamaChatModel buildDefaultModel() {
    return this
        .modelName(modelConfiguration.name())
        .baseUrl(modelConfiguration.baseUrl())
        .think(modelConfiguration.think())
        .maxRetries(modelConfiguration.maxRetries())
        .timeout(Duration.ofSeconds(modelConfiguration.timeout()))
        .logRequests(modelConfiguration.logRequests())
        .logResponses(modelConfiguration.logResponses())
        .temperature(modelConfiguration.temperature())
        .returnThinking(modelConfiguration.returnThinking())
        .topP(modelConfiguration.topP())
        .topK(modelConfiguration.topK())
        .build();
  }

  private <T> T getOrDefault(T value, T defaultValue) {
    return value != null ? value : defaultValue;
  }
}
