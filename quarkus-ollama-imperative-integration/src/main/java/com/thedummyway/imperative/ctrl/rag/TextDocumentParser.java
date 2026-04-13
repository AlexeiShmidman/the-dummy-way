package com.thedummyway.imperative.ctrl.rag;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;

import jakarta.inject.Singleton;

@Singleton
public class TextDocumentParser {

  public String extractContent(Path uploadedFile) {

    try (var fileInputStream = Files.newInputStream(uploadedFile)) {

      var parser = new AutoDetectParser();
      var handler = new BodyContentHandler();
      var metadata = new Metadata();
      var context = new ParseContext();

      parser.parse(fileInputStream, handler, metadata, context);
      return handler.toString();

    } catch (Exception exc) {
      return "";
    }
  }

}
