package ru.inno.tech.products.exceptions;

import lombok.Getter;
import ru.inno.tech.products.entities.Product;
@Getter
public class AgreementByNumberExException extends RuntimeException{
    private Product product;
    private String number;
    public AgreementByNumberExException(String message, Product product, String number) {
        super(message);
        this.product = product;
        this.number = number;
    }
}
