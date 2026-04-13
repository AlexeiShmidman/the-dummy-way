package com.thedummyway.websearch;

public class WebSearchResult {

  private final String title;
  private final String url;
  private final String snippet;

  public WebSearchResult(String title, String url, String snippet) {
      this.title = title;
      this.url = url;
      this.snippet = snippet;
  }

  public static DuckDuckGoSearchResultBuilder builder() {
      return new DuckDuckGoSearchResultBuilder();
  }

  public String getTitle() {
      return this.title;
  }

  public String getUrl() {
      return this.url;
  }

  public String getSnippet() {
      return this.snippet;
  }

  public static class DuckDuckGoSearchResultBuilder {
      private String title;
      private String url;
      private String snippet;

      DuckDuckGoSearchResultBuilder() {}

      public DuckDuckGoSearchResultBuilder title(String title) {
          this.title = title;
          return this;
      }

      public DuckDuckGoSearchResultBuilder url(String url) {
          this.url = url;
          return this;
      }

      public DuckDuckGoSearchResultBuilder snippet(String snippet) {
          this.snippet = snippet;
          return this;
      }

      public WebSearchResult build() {
          return new WebSearchResult(this.title, this.url, this.snippet);
      }
  }
}
