# OCR Document Processing Module

A Java-based OCR (Optical Character Recognition) implementation using LangChain4j and Quarkus AI services for text extraction from images.

## Components

### 1. `OcrModelConfiguration`
- **Purpose**: Configuration interface for OCR model parameters
- **Key Settings** (via `@ConfigMapping(prefix = "ai.ocr")`):
  - `name()`: Model identifier
  - `temperature()`: Generation randomness control ($$0 \leq t \leq 1$$)
  - `timeout()`: Request timeout in seconds
  - `logRequests()`/`logResponses()`: Debug logging toggles
  - `baseUrl()`: Ollama service endpoint

### 2. `OcrModelBuilder`
- **Role**: Constructs `OllamaChatModel` instances
- **Features**:
  - Singleton-scoped DI component
  - Applies configurations from `OcrModelConfiguration`
  - Configures model parameters:
    ```java
    modelBuilder
      .modelName(config.name())
      .baseUrl(config.baseUrl())
      .temperature(config.temperature())
      .timeout(Duration.ofSeconds(config.timeout()))
    ```

### 3. `OcrAssistantBuilder`
- **Function**: Creates singleton `OcrAssistant` instances
- **Dependencies**:
  - `OcrModelBuilder` for chat model
  - `@Named("aiChatMemoryProvider") ChatMemoryProvider` for conversation history
- **Implementation**:
  ```java
  synchronized OcrAssistant buildOcrAssistant() {
    if (ocrAssistant == null) {
      // Initializes Quarkus AI context with model/memory
    }
    return ocrAssistant;
  }
