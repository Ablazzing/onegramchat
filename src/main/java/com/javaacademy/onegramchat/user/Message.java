package com.javaacademy.onegramchat.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Message {
    private final String text;
    private final boolean isInbox;
    private final User userSender;
    private final User userRecipient;
}
