package com.thedummyway.imperative.ai;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.CreatedAware;

@CreatedAware
public interface ChatAssistant {

  String chat(@MemoryId Object memoryId, @UserMessage String userMessage);
}