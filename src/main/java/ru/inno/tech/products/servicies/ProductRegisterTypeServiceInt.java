package ru.inno.tech.products.servicies;

import ru.inno.tech.products.entities.AccountType;
import ru.inno.tech.products.entities.ProductClass;
import ru.inno.tech.products.entities.ProductRegisterType;
import ru.inno.tech.products.repositories.ProductRegisterTypeRepository;

import java.util.Set;

public interface ProductRegisterTypeServiceInt {
    Set<ProductRegisterType> findProdRegTypeByProdClassAndAccType(ProductClass productClass, AccountType accountType);
    ProductRegisterType findProdRegTypeByValue(String value, boolean doEx);
}
