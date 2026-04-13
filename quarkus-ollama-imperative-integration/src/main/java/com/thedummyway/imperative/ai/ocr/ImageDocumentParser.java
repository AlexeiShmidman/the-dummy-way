package com.thedummyway.imperative.ai.ocr;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import dev.langchain4j.data.image.Image;
import jakarta.inject.Singleton;

@Singleton
public class ImageDocumentParser {
  private final OcrAssistantBuilder ocrAssistantBuilder;

  public ImageDocumentParser(OcrAssistantBuilder ocrAssistantBuilder) {
    this.ocrAssistantBuilder = ocrAssistantBuilder;
  }

  public String extractContent(Path uploadedFile, String mimeType) {

    try {
      var base64Image =
          Base64.getEncoder()
              .encodeToString(Files.readAllBytes(uploadedFile));

      var image =
          Image.builder()
              .base64Data(base64Image)
              .mimeType(mimeType)
              .build();

      var fileContent = ocrAssistantBuilder.buildOcrAssistant().chat(image);
      return fileContent;

    } catch (Exception exc) {
      return "";
    }
  }
}
