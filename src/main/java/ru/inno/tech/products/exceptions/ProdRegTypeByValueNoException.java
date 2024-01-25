package ru.inno.tech.products.exceptions;

import lombok.Getter;

@Getter
public class ProdRegTypeByValueNoException extends RuntimeException{
    String value;

    public ProdRegTypeByValueNoException(String message, String value) {
        super(message);
        this.value = value;
    }
}
