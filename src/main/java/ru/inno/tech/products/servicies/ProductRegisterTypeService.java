package ru.inno.tech.products.servicies;

import org.springframework.stereotype.Service;
import ru.inno.tech.products.entities.AccountType;
import ru.inno.tech.products.entities.ProductClass;
import ru.inno.tech.products.entities.ProductRegisterType;
import ru.inno.tech.products.exceptions.ProdRegTypeByValueNoException;
import ru.inno.tech.products.repositories.ProductRegisterTypeRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class ProductRegisterTypeService implements ProductRegisterTypeServiceInt {
    ProductRegisterTypeRepository prodRegTypeRep;

    public ProductRegisterTypeService(ProductRegisterTypeRepository prodRegTypeRep) {
        this.prodRegTypeRep = prodRegTypeRep;
    }

    // Найти типы регистра для заданного в запросе класса продукта
    public Set<ProductRegisterType> findProdRegTypeByProdClassAndAccType(ProductClass productClass, AccountType accountType){
        return prodRegTypeRep.findProductRegisterTypeByProductClassAndAccountType(productClass, accountType);
    }

    // Найти тип продуктового регистра по коду типа
    public ProductRegisterType findProdRegTypeByValue(String value, boolean doEx) {
        Optional<ProductRegisterType> regTypeOp = prodRegTypeRep.findProductRegisterTypeByValue(value);
        if (regTypeOp.isEmpty()) {
            if (doEx) throw new ProdRegTypeByValueNoException(null, value);
            else return null;
        }
        return regTypeOp.get();
    }

}
