package ru.inno.tech.products.exceptions;

import lombok.Getter;

@Getter
public class ProductByIdNoException extends RuntimeException{
    Integer id;

    public ProductByIdNoException(String message, Integer id) {
        super(message);
        this.id = id;
    }
}
