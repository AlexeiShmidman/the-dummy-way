package com.thedummyway.imperative.ai.tools;

import dev.langchain4j.service.tool.ToolArgumentsErrorHandler;
import dev.langchain4j.service.tool.ToolErrorContext;
import dev.langchain4j.service.tool.ToolErrorHandlerResult;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
@Named("aiToolArgumentErrorHandler")
public class ToolArgumentErrorHandler implements ToolArgumentsErrorHandler {

  @Override
  public ToolErrorHandlerResult handle(Throwable error, ToolErrorContext context) {
    return new ToolErrorHandlerResult(
        "Error: invalid arguments provided for tool '" + context.toolExecutionRequest().name() + "'. "
            + error.getMessage()
            + ". Either provide valid arguments or use a different tool.");
  }

}
