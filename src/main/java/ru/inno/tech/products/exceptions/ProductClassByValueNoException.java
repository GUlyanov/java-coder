package ru.inno.tech.products.exceptions;

import lombok.Getter;

@Getter
public class  ProductClassByValueNoException extends RuntimeException {
    private String value;
    public ProductClassByValueNoException(String message, String value) {
        super(message);
        this.value = value;
    }
}

