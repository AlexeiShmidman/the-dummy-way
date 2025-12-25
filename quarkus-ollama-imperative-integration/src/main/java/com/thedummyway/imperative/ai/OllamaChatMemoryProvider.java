package com.thedummyway.imperative.ai;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
@Named("aiChatMemoryProvider")
public class OllamaChatMemoryProvider implements ChatMemoryProvider {

  private static final InMemoryChatMemoryStore CHAT_MEMORY_STORE = new InMemoryChatMemoryStore();

  public ChatMemoryStore getMemoryStore() {
    return CHAT_MEMORY_STORE;
  }

  @Override
  public ChatMemory get(Object memoryId) {
    return MessageWindowChatMemory.builder()//
        .id(memoryId)//
        .maxMessages(100)//
        .chatMemoryStore(CHAT_MEMORY_STORE)
        .build();
  }
}
