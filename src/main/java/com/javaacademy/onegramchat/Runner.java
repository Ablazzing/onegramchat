package com.javaacademy.onegramchat;

public class Runner {
    public static void main(String[] args) {
        try {
            OneGramChat oneGramChat = new OneGramChat();
            oneGramChat.start();
        } finally {
            OneGramChat.close();
        }
    }
}
