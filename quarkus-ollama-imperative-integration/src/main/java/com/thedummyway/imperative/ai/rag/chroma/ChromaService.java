package com.thedummyway.imperative.ai.rag.chroma;

import java.util.Optional;

import com.thedummyway.imperative.ai.rag.EmbeddingModelProvider;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;
import jakarta.inject.Singleton;

@Singleton
public class ChromaService {

  private static final Metadata DEFAULT_METADATA = new Metadata();

  private final ChromaEmbeddingStoreProvider chromaEmbeddingStoreProvider;
  private final EmbeddingModelProvider embeddingModelProvider;

  public ChromaService(ChromaEmbeddingStoreProvider chromaEmbeddingStoreProvider,
      EmbeddingModelProvider embeddingModelProvider) {
    
    this.chromaEmbeddingStoreProvider = chromaEmbeddingStoreProvider;
    this.embeddingModelProvider = embeddingModelProvider;
  }

  public void addDocument(String document, Metadata metadata) {
    
    var textSegment = TextSegment
        .from(document, Optional.ofNullable(metadata).orElse(DEFAULT_METADATA));
    
    var embedding = embeddingModelProvider
        .getOllamaEmbeddingModel()
        .embed(textSegment)
        .content();
    
    chromaEmbeddingStoreProvider
    .getChromaEmbeddingStore()
    .add(embedding, textSegment);
  }
  
  
}
