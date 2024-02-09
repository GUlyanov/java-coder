package ru.inno.tech.products.servicies;

import ru.inno.tech.products.entities.AccountType;
import ru.inno.tech.products.entities.Agreement;
import ru.inno.tech.products.entities.Product;
import ru.inno.tech.products.entities.ProductClass;
import ru.inno.tech.products.requests.ProductCreateRequestBody;
import ru.inno.tech.products.requests.ProductCreateResponseBody;

import java.util.Set;

public interface ProductServiceInt {
    Product formProduct(ProductCreateRequestBody reqBody);
    void handProduct(Product prod, Set<Agreement> agrSet, ProductCreateRequestBody reqBody);
    ProductCreateResponseBody formProdResponse(Product product);
    void saveProduct(Product product);
    Product findProductByNumber(String number, boolean doEx);
    Product findProductById(Integer id, boolean doEx);
}
