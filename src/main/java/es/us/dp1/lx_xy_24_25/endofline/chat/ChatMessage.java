package es.us.dp1.lx_xy_24_25.endofline.chat;

import java.time.Instant;

public class ChatMessage {
    private String sender;
    private String text;
    private long timestamp;

    public ChatMessage() {}

    public ChatMessage(String sender, String text) {
        this.sender = sender;
        this.text = text;
        this.timestamp = Instant.now().toEpochMilli();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
