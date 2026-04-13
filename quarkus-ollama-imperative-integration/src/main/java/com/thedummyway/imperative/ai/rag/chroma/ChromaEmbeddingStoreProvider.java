package com.thedummyway.imperative.ai.rag.chroma;

import java.time.Duration;
import java.util.Optional;

import com.thedummyway.imperative.ai.rag.RagConfiguration;

import dev.langchain4j.store.embedding.chroma.ChromaApiVersion;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
public class ChromaEmbeddingStoreProvider {

  private static final Duration DEFAULT_CHROMA_TIMEOUT = Duration.ofSeconds(10);

  private final RagConfiguration ragConfiguration;
  private ChromaEmbeddingStore chromaEmbeddingStore;

  public ChromaEmbeddingStoreProvider(RagConfiguration ragConfiguration) {
    this.ragConfiguration = ragConfiguration;
  }

  @Named("chromaEmbeddingStore")
  public synchronized ChromaEmbeddingStore getChromaEmbeddingStore() {
    if (chromaEmbeddingStore == null) {
      chromaEmbeddingStore =
          ChromaEmbeddingStore.builder()
              .baseUrl(ragConfiguration.chroma().baseUrl())
              .collectionName(ragConfiguration.chroma().collectionName())
              .apiVersion(ChromaApiVersion.V2)
              .timeout(Optional.ofNullable(ragConfiguration.chroma().timeout())
                  .map(Duration::ofSeconds)
                  .orElse(DEFAULT_CHROMA_TIMEOUT))
              .build();
    }
    return chromaEmbeddingStore;
  }
}
