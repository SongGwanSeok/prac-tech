package org.example.javaconcert.concert.presentation.dto;

import lombok.Getter;

@Getter
public class SuccessResponse<T> {

    private final String message;
    private T data;

    public SuccessResponse(String message) {
        this.message = message;
    }

    public SuccessResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static SuccessResponse<Void> withoutData(String message) {
        return new SuccessResponse<>(message);
    }

    public static <T> SuccessResponse<T> withData(String message, T data) {
        return new SuccessResponse<>(message, data);
    }
}
