package ru.inno.tech.products.exceptions;

import lombok.Getter;
import ru.inno.tech.products.entities.Product;

@Getter
public class  ProductClassByValueNoException extends RuntimeException {
    private String value;
    public ProductClassByValueNoException(String message, String value) {
        super(message);
        this.value = value;
    }
}

