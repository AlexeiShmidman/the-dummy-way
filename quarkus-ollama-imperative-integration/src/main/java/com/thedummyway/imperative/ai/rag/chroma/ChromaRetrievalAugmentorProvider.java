package com.thedummyway.imperative.ai.rag.chroma;

import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.query.router.DefaultQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
public class ChromaRetrievalAugmentorProvider {

  private final ChromaDbContentRetrieverProvider chromaDbContentRetrieverProvider;

  private RetrievalAugmentor retrievalAugmentor;

  public ChromaRetrievalAugmentorProvider(ChromaDbContentRetrieverProvider chromaDbContentRetrieverProvider) {
    this.chromaDbContentRetrieverProvider = chromaDbContentRetrieverProvider;
  }

  @Named("chromaRetrievalAugmentor")
  public synchronized RetrievalAugmentor createRetrievalAugmentor() {
    if (retrievalAugmentor == null) {
      retrievalAugmentor =
          DefaultRetrievalAugmentor.builder()
              .contentRetriever(chromaDbContentRetrieverProvider.getContentRetriever())
              .queryRouter(getQueryRouter())
              .contentInjector(DefaultContentInjector.builder().build())
              .build();
    }
    return retrievalAugmentor;
  }

  private QueryRouter getQueryRouter() {
    return new DefaultQueryRouter(chromaDbContentRetrieverProvider.getContentRetriever());
  }
}
