# RAG Chat Assistant (Java Implementation)

A Java-based Retrieval-Augmented Generation (RAG) chat assistant system that integrates language models with custom document retrieval capabilities.

## Components

### 1. `RagChatAssistantBuilder.java`
Builds and configures the RAG chat assistant with customizable components:
- Embedding model providers
- Retrieval strategies
- Language model integrations
- Response generators

### 2. `EmbeddingModelProvider.java`
Interface for text embedding models

```
public interface EmbeddingModelProvider {
    float[] embed(String text);
    List<float[]> embedBatch(List<String> texts);
}
```