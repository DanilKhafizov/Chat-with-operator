package com.galeev.operator_chat.models;

public class Bot {
    public String generateResponse(String message) {
        // Логика генерации ответа на основе входящего сообщения
        if (message.contains("привет")) {
            return "Привет! Чем могу помочь?";
        } else if (message.contains("спасибо")) {
            return "Пожалуйста! Рад был помочь.";
        } else {
            return "Извините, я не понимаю ваш запрос.";
        }
    }
}