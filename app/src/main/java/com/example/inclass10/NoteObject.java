package com.example.inclass10;

class NoteObject{
    private String message;
    private String messageID;
    private String userID;

    public NoteObject(String message, String messageID, String userID) {
        this.message = message;
        this.messageID = messageID;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
