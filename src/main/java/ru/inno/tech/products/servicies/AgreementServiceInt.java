package ru.inno.tech.products.servicies;

import ru.inno.tech.products.entities.Agreement;
import ru.inno.tech.products.entities.Product;
import ru.inno.tech.products.requests.ProductCreateRequestBody;

import java.util.Set;

public interface AgreementServiceInt {
    Set<Agreement> formAgreements(ProductCreateRequestBody reqBody);
    void saveAgreement(Agreement agreement, Product product);
    Agreement findAgreementByProductAndNumber(Product product, String number, boolean doEx);
}
