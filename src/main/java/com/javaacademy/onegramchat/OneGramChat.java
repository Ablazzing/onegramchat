package org.example.homework.four.ex4;

import org.example.homework.four.ex4.exceptions.IncorrectPasswordException;
import org.example.homework.four.ex4.exceptions.UnAuthorizeException;
import org.example.homework.four.ex4.exceptions.UserNotFoundException;

import java.util.Scanner;

public class OneGramChat {
    public static final Scanner SCANNER = new Scanner(System.in);
    private static final String MESSAGE_MASK = "письмо %s %s: %s\n";
    private static final String CHAT_COMMANDS_MESSAGE = """
            Введите команду
            "войти" - запуск функции "войти пользователю"
            "новый" - запуск функции "создать пользователя"
            "выйти" - запуск функции "выйти пользователю"
            "написать" - запуск функции "написать письмо"
            "прочитать" - запуск функции "прочитать письмо"
            "exit" - окончание работы программы
            """;
    private User[] users = new User[100];
    private int currentUsersCount = 0;
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
            System.out.println(CHAT_COMMANDS_MESSAGE);
            String commandName = SCANNER.nextLine();
            try {
                isEndOfProgram = runCommand(commandName);
            } catch (UserNotFoundException | IncorrectPasswordException | UnAuthorizeException e) {
                System.out.println(e.getMessage());
            }
        } while (isEndOfProgram);
    }

    /**
     * Запуск команды
     */
    private boolean runCommand(String commandName) throws UserNotFoundException,
            IncorrectPasswordException, UnAuthorizeException {
        switch (commandName) {
            case "войти" -> signInCommand();
            case "новый" -> createUserCommand();
            case "выйти" -> logoutCommand();
            case "написать" -> createNewMessageCommand();
            case "прочитать" -> printAllMessagesCommand();
            case "exit" -> {
                return true;
            }
            default -> System.out.println("Нет такой команды, введите заново");
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
        System.out.println("Введите имя пользователя");
        User recipient = findUserByUsername(SCANNER.nextLine());
        System.out.println("Введите текст письма");
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
        for (Message message : currentUser.getMessages()) {
            if (message == null) {
                break;
            }
            String messageType = message.isInbox() ? "от" : "к";
            System.out.printf(MESSAGE_MASK,
                    messageType,
                    message.getUserSender().getName(),
                    message.getText());
        }
    }

    /**
     * Увеличение массива пользователей (в случае переполнения)
     */
    private void incrementUsers() {
        if (currentUsersCount == users.length) {
            User[] userCopy = new User[(int) (users.length * 1.5)];
            System.arraycopy(users, 0, userCopy, 0, users.length);
            users = userCopy;
        }
    }

    /**
     * Добавить пользователя в массив пользователей
     */
    private void addUserToUsers(User user) {
        incrementUsers();
        users[currentUsersCount] = user;
        currentUsersCount++;
    }

    /**
     * Создать временного пользователя
     */
    private User createTempUser() {
        System.out.println("Введите имя пользователя");
        String username = SCANNER.nextLine();
        System.out.println("Введите пароль");
        String password = SCANNER.nextLine();
        return new User(username, password);
    }

    /**
     * Поиск пользователя по имени
     */
    private User findUserByUsername(String username) throws UserNotFoundException {
        for (User user : users) {
            if (user == null) {
                throw new UserNotFoundException("Пользователь не найден");
            }
            if (user.getName().equals(username)) {
                return user;
            }
        }
        throw new UserNotFoundException("Пользователь не найден");
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
        for (int i = 0; i < user.getMessages().length; i++) {
            if (user.getMessages()[i] == null) {
                user.getMessages()[i] = message;
                return;
            }
        }

    }
}
