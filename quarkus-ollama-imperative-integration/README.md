# Beyond Annotations: Imperative LLM Integration with Quarkus and LangChain4j

A tutorial project for AI beginners (Java) that demonstrates how to build and run a Quarkus-based project with a local Ollama installation.

The project demonstrates basic techniques for working with LLM models programmatically, namely:
- How to build a chat service (see `ChatAssistant` interface)
- How to build tools for the model (verify that the model supports tools)
- How to build Optical Character Recognition (OCR) chat
- How to build RAG with ChromaDB vector database
- Web search integration (see `WebSearchResult`)

Also, the project demonstrates how to build chat assistants in multi-context support environment.

## Project Structure and Mandatory Components

### 1. Chat Service (`ChatAssistant` and Related Builders)
- **`ChatAssistant`**: Defines the main interface for chat interactions, supporting memory-based conversations. It is implemented using Quarkus AI services and Langchain4j.
- **`ChatAssistantBuilder`**: Assembles the chat assistant, wiring in the chat model, memory provider, and tool providers. Handles error strategies for tool arguments and execution.
- **`ChatModelBuilder` & `ChatModelConfiguration`**: Configure and build the Ollama chat model, supporting custom parameters (model name, temperature, retries, etc.) via configuration.
- **`OllamaChatMemoryProvider`**: Provides memory for chat sessions, enabling context-aware conversations using an in-memory store.

### 2. Tool Integration (`ChatToolProvider` and Error Handlers)
- **`ChatToolProvider`**: Supplies tools callable by the LLM, such as string length, addition, square root, and web search. Tools are annotated and logged for traceability.
- **`ToolArgumentErrorHandler` & `ToolsExecutionErrorHandler`**: Custom error handlers for tool argument validation and execution failures, returning user-friendly error messages.

### 3. OCR Chat (`OcrAssistantBuilder`, `OcrModelBuilder`, `ImageDocumentParser`)
- **`OcrAssistantBuilder`**: Builds an assistant specialized for OCR tasks, using a dedicated model and memory provider.
- **`OcrModelBuilder` & `OcrModelConfiguration`**: Configure and build the Ollama model for OCR, supporting custom parameters.
- **`ImageDocumentParser`**: Accepts image files, encodes them, and invokes the OCR assistant to extract text content from images.

### 4. Retrieval-Augmented Generation (RAG) with ChromaDB
- **`RagChatAssistantBuilder`**: Builds a chat assistant with retrieval augmentation, combining LLM with external knowledge from ChromaDB.
- **`ChromaDbContentRetrieverProvider`**: Provides a retriever that fetches relevant content from ChromaDB using embeddings.
- **`ChromaEmbeddingStoreProvider`**: Configures and provides access to the Chroma embedding store, supporting custom base URL, collection, and timeout.
- **`ChromaRetrievalAugmentorProvider`**: Assembles the retrieval augmentor, wiring in content retrievers and query routers.
- **`ChromaService`**: Adds documents to ChromaDB, embedding them for later retrieval.
- **`RagConfiguration`**: Configuration interface for RAG, including ChromaDB and embedding model settings.

### 5. Web Search Integration
- **`WebSearchEngine` & `WebSearchResult`**: Implements web search (e.g., DuckDuckGo) as a tool callable by the LLM, returning structured search results.

## Requirements to start the project

1. Install Ollama.  
   ( refer [Ollama Download page](https://ollama.com/download) )  
   *Suggestion: pull the required model before the application run to improve start-up time (otherwise, Ollama will pull the required model at the first call). Used models are declared in [`src/main/resources/application.yml`](src/main/resources/application.yml)*
   <br>
   <br>

2. Clone the project from the repository:
   <br>
   <b><i>
   `git clone <repository-url>`
   <br>
   `cd <repository-directory>`
   </i></b>
   <br>
   <br>

3. Build and run in development mode:
   <br>
   <b><i>
   `./mvnw clean package`
   <br>
   `./mvnw quarkus:dev`
   </i></b>
   <br>
   <br>

4. Open your browser and go to chat with tool support ( see *2. Tool Integration* ):
   <br>
   <b><i>
   `http://localhost:8082/tools/chat.html`
   </i></b>
   <br>
   <br>

5. Open your browser and go to upload documents and chat with RAG:
   <br>
   <b><i>
   `http://localhost:8082/rag/rag.html`
   </i></b>
   <br>
   <br>
   *Chat with RAG also includes the demonstration of OCR feature to extract text from the images.*<br>
   *Upload accepts images, which then are passed to be OCR-ed and the extracted text is put into ChromaDB, and all files according to the [`Apache Tika`](https://tika.apache.org/3.2.3/formats.html) documentation*
   <br>

## Notes / Tips

- Make sure Ollama is running and the pulled model is available before starting the Quarkus app.
- If you change the model, update `application.yml` to point to the correct model name and version.
- For production deployment, review Quarkus and Ollama security and resource configuration.

## License

This project is licensed under the Apache 2.0 License. See the [LICENSE](https://www.apache.org/licenses/LICENSE-2.0) file for details.

## Acknowledgements
- [Quarkus](https://quarkus.io/)
- [Langchain4j](https://github.com/langchain4j/langchain4j)
- [Ollama](https://ollama.com/)
