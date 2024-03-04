package com.galeev.operator_chat.models;

import java.util.Date;

public class ChatMessage {
    public String senderId, receiverId, message, dateTime;
    public Date dateObject;
    public String conversionId, conversionName, conversionImage;
    private String messageText;
    private String messageType; // "user" или "bot"

public ChatMessage(){}

public ChatMessage(String messageText, String messageType) {
      this.messageText = messageText;
      this.messageType = messageType;
}

public String getMessageText() {
            return messageText;
        }

public String getMessageType() {
            return messageType;
        }


}

