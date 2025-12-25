# Understanding the Web Search Implementation

## Overview

This package implements a **web search engine** that integrates with LangChain4j's AI tools. It provides a custom implementation for searching the web using DuckDuckGo, which can be used by the AI assistant to fetch real-time information from the internet.

## Why a Custom Web Search?

The AI assistant in `com.dummy4dummies.programmatic.ai` package can use the `websearch` tool from `ToolProvider`. This implementation handles the actual web searching by:

1. **Scraping DuckDuckGo HTML search results** (primary method)
2. **Falling back to DuckDuckGo API** (when HTML scraping fails)
3. **Formatting results** for the AI to consume

## Core Components

### 1. **WebSearchEngine** (`WebSearchEngine.java`)
The main entry point that implements LangChain4j's web search interface.

#### Key Features:
- **Builder pattern** for easy configuration
- Integrates with LangChain4j's `WebSearchResults` structure
- Converts raw search results into AI-compatible format

#### How to use:
```java
WebSearchEngine engine = WebSearchEngine.builder()
    .duration(Duration.ofSeconds(15))
    .logRequests(true)
    .logResponses(true)
    .build();

WebSearchRequest request = WebSearchRequest.from("latest AI news");
WebSearchResults results = engine.search(request);
```

### 2. **WebClient** (`WebClient.java`)
The heart of the search functionality - handles actual HTTP requests and parsing.

#### Search Methods (in order of attempt):

1. **HTML Search** (Primary)
   - Scrapes `https://html.duckduckgo.com/html/`
   - Uses JSoup to parse HTML results
   - Most comprehensive but can be blocked

2. **API Search** (Fallback)
   - Uses `https://api.duckduckgo.com/`
   - Returns JSON format
   - More reliable but less results

3. **Default Result** (When both fail)
   - Returns a placeholder result pointing to DuckDuckGo homepage

#### Key Methods:
- `search()`: Synchronous search with retry logic
- `searchAsync()`: Asynchronous search
- `performHtmlSearch()`: Scrapes HTML results
- `performApiSearch()`: Fetches JSON API results
- `parseHtmlResults()`: Extracts data from HTML
- `parseApiResponse()`: Extracts data from JSON

### 3. **WebSearchResult** (`WebSearchResult.java`)
Simple POJO representing a single search result.

#### Fields:
- `title`: The title/link text
- `url`: The actual webpage URL
- `snippet`: A brief description/excerpt

#### Builder pattern for easy creation:
```java
WebSearchResult result = WebSearchResult.builder()
    .title("Example Domain")
    .url("https://example.com")
    .snippet("This domain is for use in illustrative examples")
    .build();
```

### 4. **Utils** (`Utils.java`)
Helper class for common operations.

#### Functions:
- `buildFormData()`: Creates URL-encoded form data from parameters
- `urlEncode()`: Properly encodes URLs (replaces `+` with `%20` for spaces)

## How Web Search Works

### Flow Diagram:
```
User/AI requests search
        ↓
WebSearchEngine.search()
        ↓
WebClient.search()
        ↓
    Try HTML Search (DuckDuckGo HTML)
        ↓ (if fails)
    Try API Search (DuckDuckGo API)
        ↓ (if both fail)
    Return default result
        ↓
WebClient returns List<WebSearchResult>
        ↓
WebSearchEngine converts to WebSearchResults
        ↓
Results returned to AI
```

### Detailed Process:

#### HTML Search Method:
1. Builds search URL with query parameters
2. Sends GET request with form data
3. Parses HTML using JSoup
4. Looks for result containers using multiple CSS selectors
5. Extracts title, URL, and snippet from each result
6. Filters and cleans URLs (converts `//` to `https://`)
7. Removes duplicates using a Set

#### API Search Method:
1. Calls DuckDuckGo API with JSON format
2. Parses JSON response
3. Extracts from multiple fields:
   - AbstractText
   - Heading
   - Answer
   - RelatedTopics
   - Results array

## Configuration Options

### WebSearchEngine.Builder:
| Parameter | Description | Default |
|-----------|-------------|---------|
| `duration` | Timeout for HTTP requests | 10 seconds |
| `logRequests` | Log HTTP requests | false |
| `logResponses` | Log HTTP responses | false |

### WebSearchRequest:
| Parameter | Description | Example |
|-----------|-------------|---------|
| `searchTerms()` | The search query | "artificial intelligence" |
| `maxResults()` | Max results to return | 5 |
| `language()` | Language/locale | "us-en" |
| `safeSearch()` | Enable safe search | true |

## Integration with AI Assistant

In the `ToolProvider` class from the previous package:
```java
@Tool("Web search")
WebSearchResults websearch(String query) {
    return WebSearchEngine.builder()
        .duration(Duration.ofSeconds(15))
        .logRequests(true)
        .logResponses(true)
        .build()
        .search(WebSearchRequest.from(query));
}
```

The AI can now use this tool when it needs current information:
- User: "What's the latest news about AI?"
- AI: Decides to call `websearch("latest AI news 2024")`
- Results are returned to the AI for summarization

## HTML Parsing Details

The HTML parser is **robust** - it tries multiple selectors to find results:

```java
String[] containers = new String[] {
    "div.result__body",        // DuckDuckGo primary
    "div.web-result",          // Alternative format
    "div.result",              // Generic result
    ".links_main",             // Another common class
    "div[data-testid='result']", // React-based results
    "div.nrn-react-div"         // Modern DuckDuckGo
};
```

### Extraction Strategy:
1. First try to find result containers
2. Then look for title elements (various selectors)
3. Extract title text and URL
4. Find snippet using multiple selectors
5. Fallback to link text if no snippet found
6. Clean and validate URLs

## Error Handling & Resilience

### Retry Logic:
- **3 retry attempts** with 1.5 second delay
- Exponential backoff handled by `RetryUtils`

### Fallback Chain:
1. HTML search → if fails → API search → if fails → Default result
2. Individual result filtering removes invalid entries
3. Empty results trigger fallback automatically

### HTTP Status Handling:
- 202 status = "blocked" by DuckDuckGo → trigger fallback
- Other errors caught and logged

## Example Output

### Raw HTML Search Result:
```html
<div class="result__body">
    <h2 class="result__title">
        <a href="https://example.com/article">
            AI Breakthrough in 2024
        </a>
    </h2>
    <div class="result__snippet">
        Researchers have achieved major breakthrough...
    </div>
</div>
```

### Parsed WebSearchResult:
```json
{
    "title": "AI Breakthrough in 2024",
    "url": "https://example.com/article",
    "snippet": "Researchers have achieved major breakthrough..."
}
```

### Final WebSearchOrganicResult (AI format):
```json
{
    "title": "AI Breakthrough in 2024",
    "url": "https://example.com/article",
    "snippet": "Researchers have achieved major breakthrough...",
    "position": 1
}
```

## Potential Issues & Solutions

1. **DuckDuckGo Blocking**
   - HTML scraping may return 202 status
   - Solution: Automatic fallback to API

2. **Incomplete Results**
   - Some results missing snippets
   - Solution: Use link text as fallback snippet

3. **URL Format Issues**
   - Relative URLs starting with `//`
   - Solution: Convert to `https://`

4. **Duplicate Results**
   - Same URL appearing multiple times
   - Solution: Track seen URLs with HashSet

## Extending the Implementation

### Adding Google as a search provider:
```java
private List<WebSearchResult> performGoogleSearch(WebSearchRequest request) {
    // Implement Google Custom Search API
    // or scrape Google HTML
}
```

### Adding result caching:
```java
private Map<String, List<WebSearchResult>> cache = new ConcurrentHashMap<>();

List<WebSearchResult> search(WebSearchRequest request) {
    String cacheKey = request.searchTerms() + "_" + request.maxResults();
    return cache.computeIfAbsent(cacheKey, k -> performSearch(request));
}
```

## Summary

This web search implementation provides:
- **Multiple search methods** with automatic fallback
- **Robust HTML parsing** that adapts to DuckDuckGo's structure
- **Clean integration** with LangChain4j's AI tool system
- **Resilient error handling** with retries
- **Configurable timeouts** and logging

The AI assistant can use this to provide **up-to-date information** from the web, making it much more useful than a static knowledge base alone.