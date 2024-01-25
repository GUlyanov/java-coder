package ru.inno.tech.products.exceptions;

import lombok.Getter;
import ru.inno.tech.products.entities.Product;

@Getter
public class ProductByNumberExException extends RuntimeException {
    private Product product;
    private String number;
    public ProductByNumberExException(String message, Product product, String number) {
        super(message);
        this.product = product;
        this.number = number;
    }
}
