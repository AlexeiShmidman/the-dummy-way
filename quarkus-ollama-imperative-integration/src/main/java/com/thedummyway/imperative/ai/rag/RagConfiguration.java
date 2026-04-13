package com.thedummyway.imperative.ai.rag;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "ai.rag")
public interface RagConfiguration {

  Integer maxResults();

  String ollamaBaseUrl();

  String embeddingModel();

  ChromaConfiguration chroma();
  
  int maxFileSize();

  interface ChromaConfiguration {

    String baseUrl();

    String collectionName();

    Long timeout();
  }
}
