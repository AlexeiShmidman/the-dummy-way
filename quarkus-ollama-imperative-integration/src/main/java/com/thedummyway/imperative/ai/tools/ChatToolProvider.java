package com.thedummyway.imperative.ai.tools;

import org.jboss.logging.Logger;

import com.thedummyway.websearch.WebSearchEngine;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.web.search.WebSearchRequest;
import dev.langchain4j.web.search.WebSearchResults;
import jakarta.inject.Singleton;

@Singleton
public class ChatToolProvider {

  private static final Logger LOG = Logger.getLogger(ChatToolProvider.class);

  @Tool("Calculate string length in characters")
  int stringLength(String s) {
    
    LOG.infov("----- Called stringLength with s=[{0}]", s);
    return s.replaceAll("\\s", "").length();
  }

  @Tool("Sum of numbers")
  int add(int a, int b) {

    LOG.infov("----- Called add with a=[{0}], b=[{1}]", a, b);
    return a + b;
  }

  @Tool("Square root")
  double sqrt(int x) {

    LOG.infov("----- Called sqrt with x=[{0}]", x);
    return Math.sqrt(x);
  }

  @Tool("Web search")
  WebSearchResults websearch(String query) {

    LOG.infov("----- Called websearch with query=[{0}]", query);

    return WebSearchEngine.builder()//
        .build()//
        .search(WebSearchRequest.from(query));
  }
}
