package com.thedummyway.imperative.ai;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.memory.ChatMemoryAccess;
import io.quarkiverse.langchain4j.CreatedAware;

@CreatedAware
public interface ChatAssistant extends ChatMemoryAccess {

  String chat(@MemoryId Object memoryId, @UserMessage String userMessage);
}
