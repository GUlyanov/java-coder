package ru.inno.tech.products.servicies;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.inno.tech.products.entities.*;
import ru.inno.tech.products.exceptions.*;
import ru.inno.tech.products.repositories.*;
import ru.inno.tech.products.requests.*;
import ru.inno.tech.products.stubes.Client;

import java.util.*;

@Service
public class ProductService {
    // репозитории
    ProductClassRepository prodClRep;
    ProductRepository prodRep;
    ProductRegisterRepository prodRegRep;
    ProductRegisterTypeRepository prodRegTypeRep;
    AccountTypeRepository accTypeRep;
    AgreementRepository agrRep;
    AccountPoolRepository accPoolRep;


    // конструктор
    public ProductService(ProductClassRepository prodClRep,
                          ProductRepository prodRep,
                          ProductRegisterRepository prodRegRep,
                          ProductRegisterTypeRepository prodRegTypeRep,
                          AccountTypeRepository accTypeRep,
                          AgreementRepository agrRep,
                          AccountPoolRepository accPoolRep) {
        this.prodClRep = prodClRep;
        this.prodRep = prodRep;
        this.prodRegRep = prodRegRep;
        this.prodRegTypeRep = prodRegTypeRep;
        this.accTypeRep = accTypeRep;
        this.agrRep = agrRep;
        this.accPoolRep = accPoolRep;
    }

    // 1.Формирование договора из текста запроса на создание - Product
    public Product formProduct(ProductCreateRequestBody reqBody){
        Integer prodId = reqBody.getInstanceId();
        if (prodId!=0){
            // если в запросе есть Id договора, заполняем поля договора из базы (так как модификация не предусмотрена)
            return findProductById(prodId, true);
        } else {
            // если ид продукта в запросе пустой, создаем новый договор
            Product prod = new Product();
            prod.setId(reqBody.getInstanceId());
            prod.setProductClass(findProductClassByValue(reqBody.getProductCode(), true));
            prod.setClientId(Client.getClientIdByMdmCode(reqBody.getMdmCode()));
            prod.setType(ProdType.valueOf(reqBody.getProductType()));
            prod.setNumber(reqBody.getContractNumber());
            prod.setPriority(reqBody.getPriority());
            prod.setDateOfConclusion(reqBody.getContractDate());
            //
            prod.setPenaltyRate(reqBody.getInterestRatePenalty());
            prod.setNso(reqBody.getMinimalBalance());
            prod.setThresholdAmount(reqBody.getThresholdAmount());
            prod.setRequisiteType(reqBody.getAccountingDetails());
            prod.setInterestRateType(reqBody.getRateType());
            prod.setTaxRate(reqBody.getTaxPercentageRate());
            //
            prod.setState("Открыт");
            return prod;
        }
    }
    // 1а.Формирование списка допсоглашений из запроса на создание договора
    public Set<Agreement> formAgreements(ProductCreateRequestBody reqBody){
        Set<Agreement> setAgr = new HashSet<>();
        List<InstanceArrangment> agrLst = reqBody.getInstanceArrangmentList();
        if (agrLst==null) return null;
        for (InstanceArrangment insArr: reqBody.getInstanceArrangmentList()) {
            Agreement agr = new Agreement();
            agr.setType(ProdType.valueOf(insArr.getArrangementType()));
            agr.setNumber(insArr.getNumber());
            agr.setStartDateTime(insArr.getOpeningDate());
            agr.setState(insArr.getStatus());
            //--
            agr.setEndDateTime(insArr.getClosingDate());
            agr.setDays(insArr.getValidityDuration());
            agr.setReasonClose(insArr.getCancellationReason());
            setAgr.add(agr);
        }
        return setAgr;
    }
    // 1б.Формирование продуктового регистра из текста запроса на создание - ProductRegister
    public ProductRegister formProdReg(ProdRegCreateRequestBody reqBody){
        // создадим новый продуктовый регистр
        ProductRegister prReg = new ProductRegister();
        // договор
        Integer prodId = reqBody.getInstanceId(); // Ид договора (обязательное поле в запросе)
        Product prod = findProductById(prodId, true);
        prReg.setProduct(prod);
        // тип регистра
        ProductRegisterType regType = findProdRegTypeByValue(reqBody.getRegistryTypeCode(), true);
        prReg.setRegisterType(regType);
        // прочие реквизиты (только код валюты)
        prReg.setCurrentCode(reqBody.getCurrencyCode());

        return prReg;
    }

    // 2.Обработка договора Product -
    @Transactional
    public void handProduct(Product prod, Set<Agreement> agrSet, ProductCreateRequestBody reqBody){
        // 1) Создание договора с одним ПР и одной валютой
        if (prod.getId()==0) {
            // 1 Проверка на наличие договора с таким номером
            findProductByNumber(prod.getNumber(), true);
            // 2 Найти типы регистра для заданного в запросе класса продукта
            AccountType accTp = findAccTypeByValue("Клиентский", true);
            Set<ProductRegisterType> regTypes = findProdRegTypeByProdClassAndAccType(prod.getProductClass(), accTp);

            // 3 Сохранить договор (product)
            saveProduct(prod);
            // 4 Сохранить типы регистра для договора
            saveProductRegister(prod, regTypes, reqBody.getIsoCurrencyCode());
            // 5 Запрос у генератора счетов счета для заданного типа регистра и привязка его к продуктовому регистру договора
            setAccountForProdReg(reqBody, prod);
        }
        // 2) Создание допсоглашений к существующему договору
        else {
            for (Agreement agr : agrSet) {
                // 1 Проверка на наличие допосоглашения с таким номером у текущего договора
                findAgreementByProductAndNumber(prod, agr.getNumber(), true);
                // 2 Сохранение допсоглашения
                saveAgreement(agr, prod);
            }
        }
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


    // 3а.Формирование ответа на запрос создания продукта
    public ProductCreateResponseBody formProdResponse(Product product){
        ProductCreateResponseBody res = new ProductCreateResponseBody();
        res.setInstanceId(product.getId()); // ид созданного договора
        //for(ProductRegister reg : findProductRegisterByProduct(product)) { // список ид созданных ПР
        for(ProductRegister reg : product.getRegisters()) { // список ид созданных ПР
            res.addRegisterId(reg.getId());
        }
        //for(Agreement agr : findAgreementByProduct(product)){ // список ид созданных ДС
        for(Agreement agr : product.getAgreements()){ // список ид созданных ДС
            res.addAgreementId(agr.getId());
        }
        return res;
    }

    // 3б.Формирование ответа на запрос создания продуктового регистра
    public ProdRegCreateResponseBody formProdRegResponse(ProductRegister prodReg){
        ProdRegCreateResponseBody res = new ProdRegCreateResponseBody();
        res.setProdRegId(prodReg.getId()); // ид созданного продуктового регистра
        return res;
    }

    // 4.Сохранение нового договора
    public void saveProduct(Product product){
        prodRep.save(product);
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

    // Сохранить допсоглашение
    public void saveAgreement(Agreement agreement, Product product){
        product.addAgreement(agreement);
        agrRep.save(agreement);
    }

    // Найти типы регистра для заданного в запросе класса продукта
    public Set<ProductRegisterType> findProdRegTypeByProdClassAndAccType(ProductClass productClass, AccountType accountType){
        return prodRegTypeRep.findProductRegisterTypeByProductClassAndAccountType(productClass, accountType);
    }

    // Найти класс продукта по коду класса продукта
    public ProductClass findProductClassByValue(String value, boolean doEx){
        Optional<ProductClass> prCl = prodClRep.findProductClassByValue(value);
        if (prCl.isEmpty()) {
            if (doEx) throw new ProductClassByValueNoException(null, value);
            else return null;
        }
        return prCl.get();
    }

    // Найти договор в базе с заданным номером
    public Product findProductByNumber(String number, boolean doEx){
        Optional<Product> prDb = prodRep.findProductByNumber(number);
        if (prDb.isPresent()) {
            if (doEx) throw new ProductByNumberExException(null, prDb.get(), number);
            else return prDb.get();
        }
        return null;
    }
    // Найти Тип счета в базе с заданным value
    public AccountType findAccTypeByValue(String number, boolean doEx){
        Optional<AccountType> accTp = accTypeRep.findAccountTypeByValue(number);
        if (accTp.isEmpty()) {
            if (doEx) throw new AccTypeByValueNoException(null, number);
            else return null;
        }
        return accTp.get();
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

    public Agreement findAgreementByProductAndNumber(Product product, String number, boolean doEx){
        Optional<Agreement> agrDb = agrRep.findAgreementByProductAndNumber(product, number);
        if (agrDb.isPresent()) {
            if (doEx) throw new AgreementByNumberExException(null, product, number);
            else return agrDb.get();
        }
        return null;
    }

    public ProductRegister findProdRegByProductAndRegType(Product product, ProductRegisterType regType, boolean doEx){
        Optional<ProductRegister> prodRegOp = prodRegRep.findProductRegisterByProductAndRegisterType(product,regType);
        if (prodRegOp.isPresent()) {
            if (doEx) throw new ProdRegByProductAndRegTypeExException(null, product, regType);
            else return prodRegOp.get();
        }
        return null;
    }

    // Очистка базы от данных, справочники не чистятся (для тестов)
    public void resetDb(){
        prodRegRep.deleteAll();
        agrRep.deleteAll();
        prodRep.deleteAll();
    }

    // Обновить продукт по Id из базы
    public Product findProductById(Integer id, boolean doEx){
        Optional<Product> prodOp = prodRep.findProductById(id);
        if (prodOp.isEmpty()) {
            if (doEx) throw new ProductByIdNoException(null, id);
            else return null;
        }
        return prodOp.get();
    }

    // Отобрать продуктовые регистры продукта
    public Set<ProductRegister> findProductRegisterByProduct(Product product){
        return prodRegRep.findProductRegisterByProduct(product);
    }

    // Очистка базы от допсоглашений, договора, ПР, справочники не чистятся (для тестов)
    public void  deleteAgreementFromDb(){
        agrRep.deleteAll();
    }
    // Удалить продуктовый регистр у договора по коду типа и номеру продукта
    @Transactional
    public void deleteProdRegByProdAndRegType(Product prod, ProductRegisterType regType){
        prodRegRep.deleteProductRegisterByProductAndRegisterType(prod, regType);
    }


    // Запрос у генератора счетов строки пула счета для заданного типа регистра и привязка его к продуктовому регистру договора
    public AccountPool getAccountPool(AccountPool accPool, boolean doEx){
        var p1 = accPool.getBranchCode();
        var p2 = accPool.getCurrencyCode();
        var p3 = accPool.getMdmCode();
        var p4 = accPool.getPriority();
        var p5 = accPool.getRegistryTypeCode();
        Optional<AccountPool> accPoolOp = accPoolRep.getAccountPool(p1, p2, p3, p4, p5);
        if (accPoolOp.isEmpty()) {
            if (doEx) throw new AccountNotFoundException(null, p1, p2, p3, p4, p5);
            else return null;
        }
        return accPoolOp.get();
    }

    // Запрос у генератора счетов номера счета для заданного типа регистра и привязка его к продуктовому регистру договора
    public String getAccount(AccountPool accPool){
        if (accPool==null) return null;
        String[] words = accPool.getAccounts().split(",");
        if(words.length==0) return null;
        int n = (new Random()).nextInt(words.length);
        return words[n].trim();
    }
    // Привязка счета к продуктовому регистру (у продукта выбираем ПР с типом, указанным в запросе)
    public void setAccountForProdReg(ProductCreateRequestBody reqBody, Product product){
        String regTypeValue = reqBody.getRegisterType();
        if (regTypeValue!=null) {
            AccountPool accPoolRec = new AccountPool(null, reqBody.getBranchCode(),
                    reqBody.getIsoCurrencyCode(), reqBody.getMdmCode(),
                    reqBody.getPriority(),
                    reqBody.getRegisterType(), null);
            AccountPool accPool = getAccountPool(accPoolRec, true);
            if(accPool==null) return;
            String accNumber = getAccount(accPool);
            for (ProductRegister prodReg : product.getRegisters()) {
                if (prodReg.getRegisterType().getValue().equals(regTypeValue)) {
                    prodReg.setAccount(accPool.getId());
                    prodReg.setAccountNumber(accNumber);
                    prodRegRep.save(prodReg);
                }
            }
        }
    }
    public void setAccountForProdReg(ProdRegCreateRequestBody reqBody, ProductRegister prodReg){
        if (prodReg!=null) {
            AccountPool accPoolRec = new AccountPool(null, reqBody.getBranchCode(),
                    reqBody.getCurrencyCode(), reqBody.getMdmCode(), reqBody.getPriorityCode(),
                    reqBody.getRegistryTypeCode(), null);
            AccountPool accPool = getAccountPool(accPoolRec, true);
            if(accPool==null) return;
            String accNumber = getAccount(accPool);
            prodReg.setAccount(accPool.getId());
            prodReg.setAccountNumber(accNumber);
            prodRegRep.save(prodReg);
        }
    }

}
