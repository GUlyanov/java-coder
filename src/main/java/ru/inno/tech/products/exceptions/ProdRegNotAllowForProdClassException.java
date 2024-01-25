package ru.inno.tech.products.exceptions;

import lombok.Getter;
import ru.inno.tech.products.entities.Product;
import ru.inno.tech.products.entities.ProductRegisterType;
@Getter
public class ProdRegNotAllowForProdClassException extends RuntimeException {
    private Product product;
    private ProductRegisterType regType;

    public ProdRegNotAllowForProdClassException(String message, Product product, ProductRegisterType regType) {
        super(message);
        this.product = product;
        this.regType = regType;
    }
}
