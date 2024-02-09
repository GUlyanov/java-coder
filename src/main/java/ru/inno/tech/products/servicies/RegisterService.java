package ru.inno.tech.products.servicies;

import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.inno.tech.products.entities.*;
import ru.inno.tech.products.exceptions.ProdRegByProductAndRegTypeExException;
import ru.inno.tech.products.exceptions.ProdRegNotAllowForProdClassException;
import ru.inno.tech.products.repositories.*;
import ru.inno.tech.products.requests.ProdRegCreateRequestBody;
import ru.inno.tech.products.requests.ProdRegCreateResponseBody;
import ru.inno.tech.products.requests.ProductCreateRequestBody;

import java.util.Optional;
import java.util.Set;
@Service
public class RegisterService implements RegisterServiceInt{
    // репозиторий
    ProductRegisterRepository prodRegRep;
    // сервисы
    ProductService prodServ;
    AccountPoolService accPoolServ;
    ProductRegisterTypeService regTypeServ;

    public RegisterService(ProductRegisterRepository prodRegRep,
                           @Lazy ProductService prodServ,
                           ProductRegisterTypeService regTypeServ,
                           AccountPoolService accPoolServ) {
        this.prodRegRep = prodRegRep;
        this.prodServ = prodServ;
        this.accPoolServ = accPoolServ;
        this.regTypeServ = regTypeServ;
    }

    // 1б.Формирование продуктового регистра из текста запроса на создание - ProductRegister
    public ProductRegister formProdReg(ProdRegCreateRequestBody reqBody){
        // создадим новый продуктовый регистр
        ProductRegister prReg = new ProductRegister();
        // договор
        Integer prodId = reqBody.getInstanceId(); // Ид договора (обязательное поле в запросе)
        Product prod = prodServ.findProductById(prodId, true);
        prReg.setProduct(prod);
        // тип регистра
        ProductRegisterType regType = regTypeServ.findProdRegTypeByValue(reqBody.getRegistryTypeCode(), true);
        prReg.setRegisterType(regType);
        // прочие реквизиты (только код валюты)
        prReg.setCurrentCode(reqBody.getCurrencyCode());

        return prReg;
    }
    // 2а.Обработка продуктового регистра ProductRegister
    @Transactional
    public void handProdReg(ProductRegister prodReg, ProdRegCreateRequestBody reqBody){
        // 1 Проверка на наличие у договора продуктового регистра с таким типом
        Product prod = prodReg.getProduct();
        ProductRegisterType regType = prodReg.getRegisterType();
        findProdRegByProductAndRegType(prod, regType, true);

        // 2 Проверка допустимости типа регистра для класса продукта
        if (prod.getProductClass()!=regType.getProductClass())
            throw new ProdRegNotAllowForProdClassException(null, prod, regType);

        // 3 Сохранить продуктовый регистр (ProductRegister)
        saveProductRegister(prodReg);

        // 4 Запрос у генератора счетов счета для заданного типа регистра и привязка его к продуктовому регистру договора
        setAccountForProdReg(reqBody, prodReg);
    }

    // 3б.Формирование ответа на запрос создания продуктового регистра
    public ProdRegCreateResponseBody formProdRegResponse(ProductRegister prodReg){
        ProdRegCreateResponseBody res = new ProdRegCreateResponseBody();
        res.setProdRegId(prodReg.getId()); // ид созданного продуктового регистра
        return res;
    }
    // 6 Сохранить типы регистра для договора
    public void saveProductRegister(Product product, Set<ProductRegisterType> registerTypes, String currentCode){
        for (ProductRegisterType regType : registerTypes){
            ProductRegister prReg = new ProductRegister(0, product, regType, 0, currentCode, null, null);
            // проверим нет ли такого ПР договора (с таким типом регистра)
            ProductRegister prRegDb = findProdRegByProductAndRegType(product,regType, false);
            if (prRegDb!=null) prReg.setId(prRegDb.getId());
            product.addProductRegister(prReg);
            regType.addProductRegister(prReg);
            prodRegRep.save(prReg);
        }
    }
    public void saveProductRegister(ProductRegister prReg){
        prodRegRep.save(prReg);
    }

    public ProductRegister findProdRegByProductAndRegType(Product product, ProductRegisterType regType, boolean doEx){
        Optional<ProductRegister> prodRegOp = prodRegRep.findProductRegisterByProductAndRegisterType(product,regType);
        if (prodRegOp.isPresent()) {
            if (doEx) throw new ProdRegByProductAndRegTypeExException(null, product, regType);
            else return prodRegOp.get();
        }
        return null;
    }

    // Отобрать продуктовые регистры продукта
    public Set<ProductRegister> findProductRegisterByProduct(Product product){
        return prodRegRep.findProductRegisterByProduct(product);
    }

    // Удалить продуктовый регистр у договора по коду типа и номеру продукта
    @Transactional
    public void deleteProdRegByProdAndRegType(Product prod, ProductRegisterType regType){
        prodRegRep.deleteProductRegisterByProductAndRegisterType(prod, regType);
    }
    // Привязка счетов к продуктовым регистрам договора
    public void setAccountForProdReg(ProductCreateRequestBody reqBody, Product product){
        // перебираем ПР договора
        for (ProductRegister prodReg : product.getRegisters()) {
            String regTypeValue = prodReg.getRegisterType().getValue();
            if (regTypeValue!=null) {
                AccountPool accPoolRec = new AccountPool(null, reqBody.getBranchCode(),
                        reqBody.getIsoCurrencyCode(), reqBody.getMdmCode(),
                        reqBody.getPriority(),
                        reqBody.getRegisterType(), null);
                AccountPool accPool = accPoolServ.getAccountPool(accPoolRec, true);
                if(accPool==null) return;
                String accNumber = accPoolServ.getAccount(accPool);
                prodReg.setAccount(accPool.getId());
                prodReg.setAccountNumber(accNumber);
                prodRegRep.save(prodReg);
            }
        }
    }
    // Привязка счета к созданому дополнительному ПР для договора
    public void setAccountForProdReg(ProdRegCreateRequestBody reqBody, ProductRegister prodReg){
        if (prodReg!=null) {
            AccountPool accPoolRec = new AccountPool(null, reqBody.getBranchCode(),
                    reqBody.getCurrencyCode(), reqBody.getMdmCode(), reqBody.getPriorityCode(),
                    prodReg.getRegisterType().getValue(), null);
            AccountPool accPool = accPoolServ.getAccountPool(accPoolRec, true);
            if(accPool==null) return;
            String accNumber = accPoolServ.getAccount(accPool);
            prodReg.setAccount(accPool.getId());
            prodReg.setAccountNumber(accNumber);
            prodRegRep.save(prodReg);
        }
    }

}
