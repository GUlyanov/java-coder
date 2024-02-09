package ru.inno.tech.products.servicies;

import ru.inno.tech.products.entities.AccountPool;
import ru.inno.tech.products.entities.Product;
import ru.inno.tech.products.entities.ProductRegister;
import ru.inno.tech.products.entities.ProductRegisterType;
import ru.inno.tech.products.requests.ProdRegCreateRequestBody;
import ru.inno.tech.products.requests.ProdRegCreateResponseBody;
import ru.inno.tech.products.requests.ProductCreateRequestBody;

import java.util.Set;

public interface RegisterServiceInt {
    ProductRegister formProdReg(ProdRegCreateRequestBody reqBody);
    void handProdReg(ProductRegister prodReg, ProdRegCreateRequestBody reqBody);
    ProdRegCreateResponseBody formProdRegResponse(ProductRegister prodReg);
    void saveProductRegister(Product product, Set<ProductRegisterType> registerTypes, String currentCode);
    void saveProductRegister(ProductRegister prReg);
    ProductRegister findProdRegByProductAndRegType(Product product, ProductRegisterType regType, boolean doEx);
    Set<ProductRegister> findProductRegisterByProduct(Product product);
    void deleteProdRegByProdAndRegType(Product prod, ProductRegisterType regType);
    void setAccountForProdReg(ProductCreateRequestBody reqBody, Product product);
    void setAccountForProdReg(ProdRegCreateRequestBody reqBody, ProductRegister prodReg);
}
