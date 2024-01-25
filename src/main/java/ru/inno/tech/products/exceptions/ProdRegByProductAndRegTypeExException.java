package ru.inno.tech.products.exceptions;

import lombok.Getter;
import ru.inno.tech.products.entities.Product;
import ru.inno.tech.products.entities.ProductRegisterType;
@Getter
public class ProdRegByProductAndRegTypeExException extends RuntimeException {
    Product product;
    ProductRegisterType regType;

    public ProdRegByProductAndRegTypeExException(String message, Product product, ProductRegisterType regType) {
        super(message);
        this.product = product;
        this.regType = regType;
    }
}
