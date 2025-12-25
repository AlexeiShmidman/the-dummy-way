package com.thedummyway.imperative.ai.rag;

import io.quarkiverse.langchain4j.ollama.OllamaEmbeddingModel;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
public class EmbeddingModelProvider {

  private final RagConfiguration ragConfiguration;

  public EmbeddingModelProvider(RagConfiguration ragConfiguration) {
    this.ragConfiguration = ragConfiguration;
  }

  @Named("defaultEmbeddingModel")
  public synchronized OllamaEmbeddingModel getOllamaEmbeddingModel() {
    return OllamaEmbeddingModel.builder()
        .baseUrl(ragConfiguration.ollamaBaseUrl())
        .model(ragConfiguration.embeddingModel())
        .build();
  }
}
