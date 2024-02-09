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
public class ProductService implements ProductServiceInt{
    // репозиторий
    ProductRepository prodRep;
    // сервисы
    RegisterService regServ;
    ProductClassService prodClServ;
    ProductRegisterTypeService regTypeServ;
    AccountTypeService accTypeServ;
    AgreementService agrServ;

    // конструктор
    public ProductService(ProductRepository prodRep,
                          ProductClassService prodClServ,
                          ProductRegisterTypeService regTypeServ,
                          AccountTypeService accTypeServ,
                          AgreementService agrServ,
                          RegisterService regServ) {
        this.prodRep = prodRep;
        this.regServ = regServ;
        this.prodClServ = prodClServ;
        this.regTypeServ = regTypeServ;
        this.accTypeServ = accTypeServ;
        this.agrServ = agrServ;
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
            prod.setProductClass(prodClServ.findProductClassByValue(reqBody.getProductCode(), true));
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

    // 2.Обработка договора Product -
    @Transactional
    public void handProduct(Product prod, Set<Agreement> agrSet, ProductCreateRequestBody reqBody){
        // 1) Создание договора с одним ПР и одной валютой
        if (prod.getId()==0) {
            // 1 Проверка на наличие договора с таким номером
            findProductByNumber(prod.getNumber(), true);
            // 2 Найти типы регистра для заданного в запросе класса продукта
            AccountType accTp = accTypeServ.findAccTypeByValue("Клиентский", true);
            Set<ProductRegisterType> regTypes = regTypeServ.findProdRegTypeByProdClassAndAccType(prod.getProductClass(), accTp);

            // 3 Сохранить договор (product)
            saveProduct(prod);
            // 4 Сохранить типы регистра для договора
            regServ.saveProductRegister(prod, regTypes, reqBody.getIsoCurrencyCode());
            // 5 Запрос у генератора счетов счета для заданного типа регистра и привязка его к продуктовому регистру договора
            regServ.setAccountForProdReg(reqBody, prod);
        }
        // 2) Создание допсоглашений к существующему договору
        else {
            for (Agreement agr : agrSet) {
                // 1 Проверка на наличие допосоглашения с таким номером у текущего договора
                agrServ.findAgreementByProductAndNumber(prod, agr.getNumber(), true);
                // 2 Сохранение допсоглашения
                agrServ.saveAgreement(agr, prod);
            }
        }
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


    // 4.Сохранение нового договора
    public void saveProduct(Product product){
        prodRep.save(product);
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

    // Обновить продукт по Id из базы
    public Product findProductById(Integer id, boolean doEx){
        Optional<Product> prodOp = prodRep.findProductById(id);
        if (prodOp.isEmpty()) {
            if (doEx) throw new ProductByIdNoException(null, id);
            else return null;
        }
        return prodOp.get();
    }


}
