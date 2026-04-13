package com.thedummyway.imperative.ai.ocr;

import com.thedummyway.imperative.ai.OcrAssistant;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import io.quarkiverse.langchain4j.QuarkusAiServicesFactory;
import io.quarkiverse.langchain4j.runtime.aiservice.QuarkusAiServiceContext;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
public class OcrAssistantBuilder {

  private final OcrModelBuilder ocrModelBuilder;
  private final ChatMemoryProvider chatMemoryProvider;
  private OcrAssistant ocrAssistant;

  public OcrAssistantBuilder(OcrModelBuilder ocrModelBuilder,
      @Named("aiChatMemoryProvider") ChatMemoryProvider chatMemoryProvider) {

    this.ocrModelBuilder = ocrModelBuilder;
    this.chatMemoryProvider = chatMemoryProvider;
  }

  public synchronized OcrAssistant buildOcrAssistant() {
    if (ocrAssistant == null) {
      var context = new QuarkusAiServiceContext(OcrAssistant.class);
      context.chatModel = ocrModelBuilder.build();
      context.initChatMemories(chatMemoryProvider);

      ocrAssistant = QuarkusAiServicesFactory.InstanceHolder.INSTANCE.<OcrAssistant>create(context).build();
    }
    return ocrAssistant;
  }
}
