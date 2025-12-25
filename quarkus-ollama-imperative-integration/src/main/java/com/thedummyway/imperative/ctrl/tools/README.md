# Chat WebSocket Controller

## What is this?

This is a chat controller that handles real-time communication between users and an AI chatbot using WebSockets. Think of it as the "traffic controller" for your chat application!


## Key Methods (The "What happens when...")

### 1. **When a user connects** (`@OnOpen`)
```java
@OnOpen
public ChatMessage onOpen() {
    return new ChatMessage(MessageType.USER_JOINED, connection.pathParam("username"),
        "Welcome " + connection.pathParam("username") + "! Connection established on " + LocalDateTime.now());
}
```
**What happens:** When someone connects to the chat
- Creates a welcome message
- Tells everyone a new user joined
- Returns: A message object with type `USER_JOINED` and a friendly greeting

### 2. **When a user sends a message** (`@OnTextMessage`)
```java
@OnTextMessage
public ChatMessage onTextMessage(ChatMessage message) {
    var aiResponse = chatAssistant.chat(connection.id(), message.message());
    return new ChatMessage(MessageType.CHAT_MESSAGE, "ChatBot", aiResponse);
}
```
**What happens:** When someone types and sends a message
- Takes the user's message
- Sends it to the AI assistant
- Gets AI's response
- Returns: The AI's reply as a chat message

### 3. **When something goes wrong** (`@OnError`)
```java
@OnError
public void onError(Throwable exc) {
    connection
        .sendText(new ChatMessage(MessageType.CHAT_MESSAGE, "ChatBot", "Exception occurred :" + exc.getMessage()));
}
```
**What happens:** If there's an error
- Catches the error
- Sends an error message back to the user

### 4. **When user disconnects** (`@OnClose`)
```java
@OnClose
public void onClose() {
    if (connection.isOpen()) {
      connection.close();
    }
}
```
**What happens:** When a user leaves
- Checks if connection is still open
- Closes it properly (cleans up)
