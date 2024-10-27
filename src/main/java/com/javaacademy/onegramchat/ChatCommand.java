package com.javaacademy.onegramchat;

import com.javaacademy.onegramchat.exceptions.CommandNotExistsException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ChatCommand {
    ENTER_USER("войти", "запуск функции \"войти пользователю\""),
    NEW_USER("новый", "запуск функции \"создать пользователя\""),
    EXIT_USER("выйти", "запуск функции \"выйти пользователю\""),
    NEW_MESSAGE("написать", "запуск функции \"написать письмо\""),
    READ_MESSAGES("прочитать", "запуск функции \"прочитать письмо\""),
    EXIT("exit", "окончание работы программы");

    private static final String COMMAND_NOT_EXISTS = "Команды с таким именем не существует";
    private final String name;
    private final String descr;

    public static ChatCommand findCommandByName(String name) {
        return Arrays.stream(ChatCommand.values())
                .filter(e -> e.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new CommandNotExistsException(COMMAND_NOT_EXISTS));
    }

    public String getFullCommandDescription() {
        return "%s - %s".formatted(name, descr);
    }
}
