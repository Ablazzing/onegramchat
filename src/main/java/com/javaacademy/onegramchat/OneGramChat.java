package com.javaacademy.onegramchat;


import com.javaacademy.onegramchat.exceptions.IncorrectPasswordException;
import com.javaacademy.onegramchat.exceptions.UnAuthorizeException;
import com.javaacademy.onegramchat.exceptions.UserAlreadyExistsException;
import com.javaacademy.onegramchat.exceptions.UserNotFoundException;
import com.javaacademy.onegramchat.user.Message;
import com.javaacademy.onegramchat.user.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.javaacademy.onegramchat.ChatCommand.findCommandByName;
import static com.javaacademy.onegramchat.ChatMessage.INPUT_MESSAGE;
import static com.javaacademy.onegramchat.ChatMessage.INPUT_PASSWORD;
import static com.javaacademy.onegramchat.ChatMessage.INPUT_USERNAME;
import static com.javaacademy.onegramchat.ChatMessage.NO_COMMAND_TYPE;

public class OneGramChat {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String MESSAGE_MASK = "письмо %s %s: %s\n";
    private static final String CHAT_COMMANDS_MESSAGE = "Введите команду\n";

    private final Map<String, User> users = new HashMap<>();
    private User currentUser = null;

    /**
     * Закрытие потока ввода
     */
    public static void close() {
        SCANNER.close();
    }

    /**
     * Запуск чата
     */
    public void start() {
        System.out.println("Добро пожаловать в OneGramChat!");
        boolean isEndOfProgram = false;
        do {
            printCommands();
            String commandName = SCANNER.nextLine();
            try {
                ChatCommand chatCommand = findCommandByName(commandName);
                isEndOfProgram = runCommand(chatCommand);
            } catch (UserNotFoundException | IncorrectPasswordException | UnAuthorizeException e) {
                System.out.println(e.getMessage());
            }
        } while (!isEndOfProgram);
    }

    /**
     * Печать команд
     */
    private void printCommands() {
        StringBuilder stringBuilder = new StringBuilder(CHAT_COMMANDS_MESSAGE);
        Arrays.stream(ChatCommand.values())
                .forEach(chatCommand -> stringBuilder.append(chatCommand.getFullCommandDescription()).append("\n"));
        System.out.println(stringBuilder);
    }

    /**
     * Запуск команды
     */
    private boolean runCommand(ChatCommand chatCommand) throws UserNotFoundException,
            IncorrectPasswordException, UnAuthorizeException {
        switch (chatCommand) {
            case ENTER_USER -> signInCommand();
            case NEW_USER -> createUserCommand();
            case EXIT_USER -> logoutCommand();
            case NEW_MESSAGE -> createNewMessageCommand();
            case READ_MESSAGES -> printAllMessagesCommand();
            case EXIT -> {
                return true;
            }
            default -> System.out.println(NO_COMMAND_TYPE);
        }
        return false;
    }

    /**
     * Создать пользователя
     */
    private void createUserCommand() {
        addUserToUsers(createTempUser());
    }

    /**
     * Вход пользователя
     */
    private void signInCommand() throws UserNotFoundException, IncorrectPasswordException {
        User tempUser = createTempUser();
        User user = findUserByUsername(tempUser.getName());
        checkUserPassword(user, tempUser.getPassword());
    }

    /**
     * Создание нового письма
     */
    private void createNewMessageCommand() throws UnAuthorizeException, UserNotFoundException {
        checkAuthorization();
        System.out.println(INPUT_USERNAME);
        User recipient = findUserByUsername(SCANNER.nextLine());
        System.out.println(INPUT_MESSAGE);
        String text = SCANNER.nextLine();
        writeMessage(text, currentUser, recipient);
    }

    /**
     * Выйти пользователю
     */
    private void logoutCommand() {
        currentUser = null;
    }

    /**
     * Распечатать все сообщения пользователя
     */
    private void printAllMessagesCommand() throws UnAuthorizeException {
        checkAuthorization();
        currentUser.getMessages().forEach(this::printMessage);
    }

    /**
     * Печать сообщения
     */
    private void printMessage(Message message) {
        String messageType = message.isInbox() ? "от" : "к";
        System.out.printf(MESSAGE_MASK,
                messageType,
                message.getUserSender().getName(),
                message.getText());
    }

    /**
     * Добавить пользователя в массив пользователей
     */
    private void addUserToUsers(User user) {
        if (users.containsKey(user.getName())) {
            throw new UserAlreadyExistsException();
        }
        users.put(user.getName(), user);
    }

    /**
     * Создать временного пользователя
     */
    private User createTempUser() {
        System.out.println(INPUT_USERNAME);
        String username = SCANNER.nextLine();
        System.out.println(INPUT_PASSWORD);
        String password = SCANNER.nextLine();
        return new User(username, password);
    }

    /**
     * Поиск пользователя по имени
     */
    private User findUserByUsername(String username) throws UserNotFoundException {
        User user = users.get(username);
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return user;
    }

    /**
     * Проверка имени пользователя и пароля
     */
    private void checkUserPassword(User user, String password) throws IncorrectPasswordException {
        if (user.getPassword().equals(password)) {
            currentUser = user;
        } else {
            throw new IncorrectPasswordException("Некорректный пароль для пользователя");
        }
    }

    /**
     * Проверка авторизации
     */
    private void checkAuthorization() throws UnAuthorizeException {
        if (currentUser == null) {
            throw new UnAuthorizeException("Пользователь не авторизован");
        }
    }

    /**
     * Написать сообщение пользователям
     */
    private void writeMessage(String textMessage, User userSender, User userRecipient) {
        Message messageOut = new Message(textMessage, false, userSender, userRecipient);
        Message messageIn = new Message(textMessage, true, userSender, userRecipient);
        addMessageToUser(messageOut, userSender);
        addMessageToUser(messageIn, userRecipient);
    }

    /**
     * Добавить сообщение пользователю
     */
    private void addMessageToUser(Message message, User user) {
        user.getMessages().add(message);
    }
}
