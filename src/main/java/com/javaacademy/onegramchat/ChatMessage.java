package com.javaacademy.onegramchat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChatMessage {
    INPUT_USERNAME("Введите имя пользователя"),
    INPUT_PASSWORD("Введите пароль"),
    INPUT_MESSAGE("Введите текст письма"),
    NO_COMMAND_TYPE("Нет такой команды, введите заново");

    private final String message;

    @Override
    public String toString() {
        return message;
    }
}
