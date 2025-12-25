# Understanding the LangChain4j AI Chat Assistant Components Implementation

## Overview

This project implements an AI-powered chat assistant using LangChain4j with Ollama as the underlying LLM. The code is structured to provide a flexible, configurable assistant that can maintain conversation memory and use custom tools.

## Core Components

### 1. **ChatAssistantBuilder** (`ChatAssistantBuilder.java`)
This is the **factory class** that constructs configured AI assistants.

#### What it does:
- Injects all necessary dependencies (error handlers, memory, tools, model config)
- Builds an `Assistant` instance using LangChain4j's `AiServices.builder()`
- Configures:
  - **Chat Model**: Which LLM to use (via `ChatModelBuilder`)
  - **Memory Provider**: How to store conversation history
  - **Tools**: What functions the AI can call
  - **Error Handlers**: How to handle tool-related errors
  - **Hallucinated Tool Strategy**: What to do when AI tries to use non-existent tools

### 2. **ChatModelBuilder** (`ChatModelBuilder.java`)
Builds the actual LLM client (Ollama in this case).

#### Features:
- **Fluent builder pattern** for easy configuration
- Two build methods:
  - `build()`: Uses manually set parameters
  - `buildDefaultModel()`: Uses configuration from `ModelConfiguration`
- Configurable parameters include:
  - Model name, base URL, temperature, timeout, logging, etc.

### 3. **ChatModelConfiguration** (`ChatModelConfiguration.java`)
Configuration interface using SmallRye Config.

#### Properties explained:
```properties
ai.model.name=llama2              # The Ollama model to use
ai.model.temperature=0.7           # Creativity (0=deterministic, 1=creative)
ai.model.topP=0.9                   # Nucleus sampling parameter
ai.model.topK=40                    # Top-k sampling parameter
ai.model.timeout=60                 # API timeout in seconds
ai.model.logResponses=true          # Log LLM responses for debugging
ai.model.logRequests=true            # Log API requests for debugging
ai.model.think=true                  # Enable/disable model thinking
ai.model.returnThinking=false        # Return thinking process in response
ai.model.baseUrl=http://localhost:11434  # Ollama API URL
ai.model.maxRetries=3                # Retry attempts on failure
```

### 5. **OllamaChatMemoryProvider** (`OllamaChatMemoryProvider.java`)
Manages conversation memory. The provider is required due to the following code in `ChatMemoryService` class:

    public ChatMemory getOrCreateChatMemory(Object memoryId) {
        if (memoryId == DEFAULT) {
            if (defaultChatMemory == null) {
                defaultChatMemory = chatMemoryProvider.get(DEFAULT);
            }
            return defaultChatMemory;
        }
        return chatMemories.computeIfAbsent(memoryId, chatMemoryProvider::get);
    }

If no chat memory provider present, then NULL-pointer exception is raised

#### Key points:
- Implements LangChain4j's `ChatMemoryProvider`
- Uses **in-memory storage** (messages lost on restart)
- Each conversation has its own memory identified by `memoryId`
- Retains last **100 messages** per conversation
- `@Named("aiChatMemoryProvider")`: Makes this bean available for injection

### 6. **ChatToolProvider** (`ChatToolProvider.java`)
Provides tools/functions the AI can use.

#### Available tools:
- `stringLength`: Counts characters (excluding spaces)
- `add`: Adds two numbers
- `sqrt`: Calculates square root
- `websearch`: Performs web searches

#### Important:
- Each tool is annotated with `@Tool` and a description
- The AI decides when to use these tools based on the description
- Logging helps debug when tools are called

## How It All Works Together

### Flow of a User Request:
1. **User sends message** with a `memoryId` (e.g., user session ID)
2. **ChatAssistantBuilder** provides the configured `Assistant`
3. **Memory provider** retrieves or creates conversation history for that `memoryId`
4. **Chat model** (Ollama) processes the message with context
5. **AI may decide to use tools** from `ToolProvider`
6. **Tools execute** and return results
7. **Final response** is generated and returned

### Dependency Injection Flow:
```
ChatAssistantBuilder
├── ToolArgumentsErrorHandler
├── ToolExecutionErrorHandler
├── ChatMemoryProvider (OllamaChatMemoryProvider)
├── ChatToolProvider
└── ChatModelBuilder
    └── ChatModelConfiguration
```

## Configuration Examples

### application.properties:
```properties
# Model settings
ai.model.name=mistral
ai.model.temperature=0.5
ai.model.baseUrl=http://localhost:11434
ai.model.timeout=30

# Memory settings (hardcoded in provider)
# Currently set to 100 messages max
```

## Common Use Cases

1. **Multi-user chat application**
   - Use different `memoryId` per user
   - Each user has isolated conversation history

2. **Tool-enabled assistant**
   - AI can calculate, search web, etc.
   - Tools extend AI's capabilities beyond text generation

3. **Debugging AI behavior**
   - Enable request/response logging
   - Monitor tool calls via logs

## Potential Gotchas

1. **In-memory storage**: All chat history is lost on application restart
2. **Tool execution**: AI might misuse tools if descriptions aren't clear
3. **Error handling**: Custom error handlers manage tool failures
4. **Hallucinated tools**: Strategy defined for when AI invents tools

## Extending the System

### Adding a new tool:
```java
@Tool("Description of what the tool does")
ReturnType toolName(ParameterType param) {
    // Implementation
}
```

### Changing memory provider:
Implement `ChatMemoryProvider` with a persistent store (database, Redis, etc.)

### Using different LLM provider:
Modify `ChatModelBuilder` to use OpenAI, HuggingFace, etc. instead of Ollama.

