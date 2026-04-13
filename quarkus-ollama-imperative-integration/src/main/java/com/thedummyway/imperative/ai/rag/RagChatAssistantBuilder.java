package com.thedummyway.imperative.ai.rag;

import com.thedummyway.imperative.ai.ChatAssistant;
import com.thedummyway.imperative.ai.tools.ChatModelBuilder;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.rag.RetrievalAugmentor;
import io.quarkiverse.langchain4j.QuarkusAiServicesFactory;
import io.quarkiverse.langchain4j.runtime.aiservice.QuarkusAiServiceContext;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
public class RagChatAssistantBuilder {

  private final RetrievalAugmentor retrievalAugmentor;
  private final ChatModelBuilder chatModelBuilder;
  private final ChatMemoryProvider chatMemoryProvider;

  public RagChatAssistantBuilder(@Named("chromaRetrievalAugmentor") RetrievalAugmentor retrievalAugmentor,
      @Named("aiChatMemoryProvider") ChatMemoryProvider chatMemoryProvider,
      ChatModelBuilder chatModelBuilder) {

    this.retrievalAugmentor = retrievalAugmentor;
    this.chatModelBuilder = chatModelBuilder;
    this.chatMemoryProvider = chatMemoryProvider;
  }

  public ChatAssistant buildChatAssistant() {
    var context = new QuarkusAiServiceContext(ChatAssistant.class);
    context.chatModel = chatModelBuilder.buildDefaultModel();
    context.retrievalAugmentor = retrievalAugmentor;
    context.initChatMemories(chatMemoryProvider);

    return QuarkusAiServicesFactory.InstanceHolder.INSTANCE.<ChatAssistant>create(context).build();
  }
}
