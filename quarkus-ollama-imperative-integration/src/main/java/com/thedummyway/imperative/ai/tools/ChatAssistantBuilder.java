package com.thedummyway.imperative.ai.tools;

import java.util.List;

import com.thedummyway.imperative.ai.ChatAssistant;

import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.service.tool.ToolArgumentsErrorHandler;
import dev.langchain4j.service.tool.ToolExecutionErrorHandler;
import io.quarkiverse.langchain4j.QuarkusAiServicesFactory;
import io.quarkiverse.langchain4j.runtime.aiservice.QuarkusAiServiceContext;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
public class ChatAssistantBuilder {

  private final ToolArgumentsErrorHandler toolArgumentErrorHandler;
  private final ToolExecutionErrorHandler toolExecutionErrorHandler;
  private final ChatMemoryProvider chatMemoryProvider;
  private final ChatToolProvider toolProvider;
  private final ChatModelBuilder chatModelBuilder;

  public ChatAssistantBuilder(
      @Named("aiToolArgumentErrorHandler") ToolArgumentsErrorHandler toolArgumentErrorHandler,
      @Named("aiToolExecutionErrorHandler") ToolExecutionErrorHandler toolExecutionErrorHandler,
      @Named("aiChatMemoryProvider") ChatMemoryProvider chatMemoryProvider,
      ChatToolProvider toolProvider,
      ChatModelBuilder chatModelBuilder) {

    this.toolArgumentErrorHandler = toolArgumentErrorHandler;
    this.toolExecutionErrorHandler = toolExecutionErrorHandler;
    this.chatMemoryProvider = chatMemoryProvider;
    this.toolProvider = toolProvider;
    this.chatModelBuilder = chatModelBuilder;
  }

  public ChatAssistant buildChatAssistantWithTools() {
    var context = new QuarkusAiServiceContext(ChatAssistant.class);
    context.chatModel = chatModelBuilder.buildDefaultModel();
    context.initChatMemories(chatMemoryProvider);
    context.toolService.tools(List.of(toolProvider));
    context.toolService.argumentsErrorHandler(toolArgumentErrorHandler);
    context.toolService.executionErrorHandler(toolExecutionErrorHandler);
    context.toolService
        .hallucinatedToolNameStrategy(toolExecutionRequest -> ToolExecutionResultMessage.from(toolExecutionRequest,
            "Error: there is no tool with the following parameters called " +
                toolExecutionRequest.name()));

    return QuarkusAiServicesFactory.InstanceHolder.INSTANCE.<ChatAssistant>create(context).build();
  }
}
