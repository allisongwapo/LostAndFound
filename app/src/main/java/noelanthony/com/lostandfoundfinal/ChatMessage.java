package noelanthony.com.lostandfoundfinal;

public class ChatMessage {
    private String message;
    private String senderId;
    private String receiverId;
    private String senderName;
    private String status;
    private String time;

    public ChatMessage() {
    }

    public ChatMessage(String message, String senderId, String receiverId, String senderName, String status, String time) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.status = status;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderName() { return senderName; }

    public void setSenderName(String senderName) { this.senderName =senderName; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getTime() {return time; }

    public void setTime(String time ) { this.time = time; }


}
