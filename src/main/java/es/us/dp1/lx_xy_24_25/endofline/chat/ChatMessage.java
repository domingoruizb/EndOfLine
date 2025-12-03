package es.us.dp1.lx_xy_24_25.endofline.chat;

import java.time.Instant;

// DTO for returning messages to player
// 'body' for message text, 'timestamp' as epoch milliseconds 
public class ChatMessage {
    private String sender;
    private String body;
    private long timestamp;

    public ChatMessage() {}

    public ChatMessage(String sender, String body) {
        this.sender = sender;
        this.body = body;
        this.timestamp = Instant.now().toEpochMilli();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
