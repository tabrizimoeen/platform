package org.platform.repair.dto.bale;

public class BaleUpdate {
    private Message message;

    // Getters and Setters
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    // Inner class for Message
    public static class Message {
        private Chat chat;
        private String text;
        private Long date; // تاریخ ارسال پیام

        // Getters and Setters for Message fields
        public Chat getChat() {
            return chat;
        }

        public void setChat(Chat chat) {
            this.chat = chat;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Long getDate() {
            return date;
        }

        public void setDate(Long date) {
            this.date = date;
        }
    }

    // Inner class for Chat
    public static class Chat {
        private Long id;
        private String type; // 'private', 'group', 'supergroup', 'channel'
        private String firstName;
        private String lastName;
        private String username;

        // Getters and Setters for Chat fields
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
