package com.thedummyway.imperative.ai.rag.chroma;

import java.util.Optional;

import com.thedummyway.imperative.ai.rag.RagConfiguration;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
public class ChromaDbContentRetrieverProvider {

  private static final Integer DEFAULT_MAX_RESULTS = Integer.valueOf(10);

  private final ChromaEmbeddingStore embeddingStore;
  private final EmbeddingModel embeddingModel;
  private final RagConfiguration ragConfiguration;

  private ContentRetriever contentRetriever;

  public ChromaDbContentRetrieverProvider(
      @Named("chromaEmbeddingStore") ChromaEmbeddingStore embeddingStore,
      @Named("defaultEmbeddingModel") EmbeddingModel embeddingModel,
      RagConfiguration ragConfiguration) {

    this.embeddingStore = embeddingStore;
    this.embeddingModel = embeddingModel;
    this.ragConfiguration = ragConfiguration;
  }

  public synchronized ContentRetriever getContentRetriever() {
    if (contentRetriever == null) {
      contentRetriever = EmbeddingStoreContentRetriever.builder()
      .embeddingModel(embeddingModel)
      .embeddingStore(embeddingStore)
      .maxResults(Optional.ofNullable(ragConfiguration.maxResults()).orElse(DEFAULT_MAX_RESULTS))
      .build();
    }
    return contentRetriever;
  }
}
