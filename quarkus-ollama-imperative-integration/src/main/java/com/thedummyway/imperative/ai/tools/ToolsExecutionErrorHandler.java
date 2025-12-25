package com.thedummyway.imperative.ai.tools;

import dev.langchain4j.service.tool.ToolErrorContext;
import dev.langchain4j.service.tool.ToolErrorHandlerResult;
import dev.langchain4j.service.tool.ToolExecutionErrorHandler;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
@Named(value = "aiToolExecutionErrorHandler")
public class ToolsExecutionErrorHandler implements ToolExecutionErrorHandler{

  @Override
  public ToolErrorHandlerResult handle(Throwable error, ToolErrorContext context) {
    return new ToolErrorHandlerResult(
        "Error: execution failed for '" + context.toolExecutionRequest().name() + "'. "
            + error.getMessage()
            + ". Either provide valid arguments or use a different tool.");
  }

}
