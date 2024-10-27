package com.javaacademy.onegramchat;

public class Message {
    //Создать сообщение (текст, входящее/исходящее, кому/от кого)
    private final String text;
    private final boolean isInbox;
    private final User userSender;
    private final User userRecipient;

    public Message(String text, boolean isInbox, User userSender, User userRecipient) {
        this.text = text;
        this.isInbox = isInbox;
        this.userSender = userSender;
        this.userRecipient = userRecipient;
    }

    public String getText() {
        return text;
    }

    public boolean isInbox() {
        return isInbox;
    }

    public User getUserSender() {
        return userSender;
    }

    public User getUserRecipient() {
        return userRecipient;
    }
}
