package com.javaacademy.onegramchat.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public class User {
    private final String name;
    private final String password;
    private List<Message> messages = new ArrayList<>();
}
