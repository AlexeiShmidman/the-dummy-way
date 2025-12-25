package com.thedummyway.imperative.ctrl.tools;

import java.time.LocalDateTime;

import com.thedummyway.imperative.ai.ChatAssistant;
import com.thedummyway.imperative.ai.tools.ChatAssistantBuilder;

import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnError;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;

@WebSocket(path = "/llm/chat/{username}")
public class ChatWebSocketController {

  private final WebSocketConnection connection;
  private final ChatAssistant chatAssistant;

  public ChatWebSocketController(ChatAssistantBuilder chatAssistantBuilder,
      WebSocketConnection connection) {

    this.connection = connection;
    this.chatAssistant = chatAssistantBuilder.buildChatAssistantWithTools();
  }

  @OnOpen
  public ChatMessage onOpen() {
    return new ChatMessage(MessageType.USER_JOINED, connection.pathParam("username"),
        "Welcome " + connection.pathParam("username") + "! Connection established on " + LocalDateTime.now());
  }

  @OnTextMessage
  public ChatMessage onTextMessage(ChatMessage message) {
    var aiResponse = chatAssistant.chat(connection.id(), message.message());
    return new ChatMessage(MessageType.CHAT_MESSAGE, "ChatBot", aiResponse);
  }

  @OnError
  public void onError(Throwable exc) {
    connection
        .sendText(new ChatMessage(MessageType.CHAT_MESSAGE, "ChatBot", "Exception occurred :" + exc.getMessage()));
  }

  @OnClose
  public void onClose() {
    if (connection.isOpen()) {
      connection.close();
    }
  }
}
