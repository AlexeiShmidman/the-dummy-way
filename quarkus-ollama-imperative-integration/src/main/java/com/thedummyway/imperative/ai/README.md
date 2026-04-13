### 1. **Chat Assistant Interface** (`ChatAssistant.java`)
```java
public interface ChatAssistant {
    String chat(@MemoryId Object memoryId, @UserMessage String userMessage);
}
```
This is an interface that LangChain4j uses to create the AI chat service with a chat memory:
- `@MemoryId`: Identifies different conversation threads (each user/conversation gets its own memory)
- `@UserMessage`: The actual user input

### 2. **Optical Character Recognition (OCR) Assistant Interface** (`OcrAssistant.java`)
```java
public interface OcrAssistant {

  @UserMessage("Extract text from the given image")
  String chat(Image file);
}
```
This is an interface that LangChain4j uses to create the AI service that accepts an image to send to a LLM that supports OCR (e.g. glm-ocr:bf16)

**Key concept**: LangChain4j automatically implements these interfaces based on configuration when a context for a chat is created
