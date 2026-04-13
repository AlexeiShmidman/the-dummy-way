# ChromaDB AI Integration Toolkit

This project provides Java-based integration components for ChromaDB, an open-source vector database for AI applications. The toolkit includes providers for embedding storage, content retrieval, and retrieval augmentation using ChromaDB.

## Key Components

### 1. ChromaService
Core service handling ChromaDB operations:
- Collection management (`createCollection`, `collectionExists`)
- Embedding operations (`add`, `query`)
- Direct API interaction via `ChromaClientWrapper`

### 2. ChromaEmbeddingStoreProvider
Creates an `EmbeddingStore` implementation for ChromaDB:
- Implements `EmbeddingStore` interface from LangChain4j
- Handles embedding storage using `ChromaService`
- Supports metedata filtering during queries

### 3. ChromaDbContentRetrieverProvider
Provides ChromaDB-based `ContentRetriever`:
- Transforms text queries using `EmbeddingModel`
- Retrieves relevant content through `EmbeddingStore`
- Configurable max results and similarity thresholds

### 4. ChromaRetrievalAugmentorProvider
Creates `RetrievalAugmentor` for RAG workflows:
- Wraps `ContentRetriever` implementation
- Enhances queries with relevant context from ChromaDB
- Supports dynamic prompt augmentation

## Dependency Management
All components require:
- `chroma-client-wrapper` for ChromaDB API communication
- `langchain4j` core library
- `retrieval-augmentor` module

```java
// Example dependency configuration
dependencies {
    implementation "dev.langchain4j:langchain4j-core:0.25.0"
    implementation "ai.chroma:chroma-client-wrapper:1.4.2"
}
