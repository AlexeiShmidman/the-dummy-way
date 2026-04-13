package com.thedummyway.imperative.ctrl.rag;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.thedummyway.imperative.ai.ChatAssistant;
import com.thedummyway.imperative.ai.ocr.ImageDocumentParser;
import com.thedummyway.imperative.ai.rag.RagChatAssistantBuilder;
import com.thedummyway.imperative.ai.rag.RagConfiguration;
import com.thedummyway.imperative.ai.rag.chroma.ChromaService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/rag")
public class RagChatController {

  private final ChromaService chromaService;
  private final TextDocumentParser textDocumentParser;
  private final ImageDocumentParser imageDocumentParser;
  private final ChatAssistant chatAssistant;
  private final RagConfiguration ragConfiguration;

  public RagChatController(ChromaService chromaService,
      TextDocumentParser textDocumentParser,
      ImageDocumentParser imageDocumentParser,
      RagChatAssistantBuilder ragChatAssistantBuilder,
      RagConfiguration ragConfiguration) {

    this.chromaService = chromaService;
    this.textDocumentParser = textDocumentParser;
    this.imageDocumentParser = imageDocumentParser;
    this.ragConfiguration = ragConfiguration;
    this.chatAssistant = ragChatAssistantBuilder.buildChatAssistant();
  }

  @POST
  @Path("/upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public Response uploadFiles(@FormParam("files") List<FileUpload> files) {

    if (files == null || files.isEmpty()) {
      return Response.status(Response.Status.BAD_REQUEST)
          .build();
    }

    for (FileUpload file : files) {

      try {

        if (file.size() > (ragConfiguration.maxFileSize() * 1024 * 1024)) {
          continue;
        }

        var mimeType = file.contentType();
        var fileContent =
            mimeType.startsWith("image/")
                ? imageDocumentParser.extractContent(file.uploadedFile(), mimeType)
                : textDocumentParser.extractContent(file.uploadedFile());

        if (Objects.nonNull(fileContent) && !fileContent.isBlank()) {
          chromaService.addDocument(fileContent, null);
        }

      } catch (Exception e) {
        continue;
      }
    }

    return Response.ok().build();
  }

  @POST
  @Path("/chat")
  public String chat(String prompt) {
    try {
      var response = chatAssistant.chat(UUID.randomUUID(), prompt);
      return response;

    } catch (Throwable exc) {
      return "Exception for answering prompt=[%s]. \n\t %s".formatted(prompt, exc.getMessage());
    }
  }

}
