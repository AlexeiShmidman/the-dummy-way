package com.thedummyway.imperative.ai.ocr;

import java.time.Duration;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel.OllamaChatModelBuilder;
import jakarta.inject.Singleton;

@Singleton
public class OcrModelBuilder {

  private final OcrModelConfiguration modelConfiguration;

  public OcrModelBuilder(OcrModelConfiguration modelConfiguration) {
    this.modelConfiguration = modelConfiguration;
  }


  public OllamaChatModel build() {
    var modelBuilder = new OllamaChatModelBuilder();
    return modelBuilder
        .modelName(modelConfiguration.name())
        .baseUrl(modelConfiguration.baseUrl())
        .think(modelConfiguration.think())
        .maxRetries(modelConfiguration.maxRetries())
        .timeout(Duration.ofSeconds(modelConfiguration.timeout()))
        .logRequests(modelConfiguration.logRequests())
        .logResponses(modelConfiguration.logResponses())
        .temperature(modelConfiguration.temperature())
        .build();
  }
}
