package ru.inno.tech.products.exceptions;

import lombok.Getter;

@Getter
public class AccTypeByValueNoException extends RuntimeException {
    private String value;
    public AccTypeByValueNoException(String message, String value) {
        super(message);
        this.value = value;
    }

}
