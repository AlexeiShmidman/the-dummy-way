package com.thedummyway.imperative.ai;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.CreatedAware;

@CreatedAware
public interface OcrAssistant {

  @UserMessage("Extract text from the given image")
  String chat(Image file);
}
