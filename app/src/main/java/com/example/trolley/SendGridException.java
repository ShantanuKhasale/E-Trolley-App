package com.example.trolley;

public class SendGridException extends Exception {
    public SendGridException(String errorMessage) {
        super(errorMessage);
    }
}