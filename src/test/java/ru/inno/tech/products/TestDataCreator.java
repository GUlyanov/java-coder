package ru.inno.tech.products;

import ru.inno.tech.products.entities.ProdType;
import ru.inno.tech.products.entities.Product;
import ru.inno.tech.products.entities.ProductClass;
import ru.inno.tech.products.requests.AdditionalProperty;
import ru.inno.tech.products.requests.InstanceArrangment;
import ru.inno.tech.products.requests.ProdRegCreateRequestBody;
import ru.inno.tech.products.requests.ProductCreateRequestBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TestDataCreator {
    //========  Вспомогательные методы - Создание объектов-тел запросов ===============================================
    // Тело запроса на создание договора без допсоглашений
    public static ProductCreateRequestBody crProdCrReqBod(){
        ProductCreateRequestBody req = new ProductCreateRequestBody();
        req.setInstanceId(0);
        req.setProductType("ДОГОВОР");
        req.setProductCode("02.001.005");
        req.setRegisterType("02.001.005_45343_CoDowFF");
        req.setMdmCode("13");
        req.setContractNumber("123/456");
        req.setContractDate(LocalDate.of(2019,12,25));
        req.setPriority("00");
        req.setInterestRatePenalty(BigDecimal.valueOf(11.34));
        req.setMinimalBalance(BigDecimal.valueOf(10500.00));
        req.setThresholdAmount(BigDecimal.valueOf(15000.00));
        req.setAccountingDetails("Имеются ограничения");
        req.setRateType("Обычная");
        req.setTaxPercentageRate(BigDecimal.valueOf(15.33));
        req.setTechnicalOverdraftLimitAmount(BigDecimal.valueOf(10.77));
        req.setContractId(1770);
        req.setBranchCode("0021");
        req.setIsoCurrencyCode("500");
        req.setUrgencyCode("00");
        req.setReferenceCode(3675);
        req.setAdditionalPropertyList(
                List.of(new AdditionalProperty("RailwayRegionOwn", "ABC", "Регион принадлежности железной дороги"),
                        new AdditionalProperty("counter", "123", "Счетчик")));
        req.setInstanceArrangmentList(null);
        return req;
    }

    // Тело запроса на создание двух допсоглашений для существующего договора
    public static ProductCreateRequestBody crAgrsCrReqBod(){
        ProductCreateRequestBody req = crProdCrReqBod();
        req.setInstanceArrangmentList(
                List.of(new InstanceArrangment(
                                null,null, "НСО",null,
                                "15/2", LocalDateTime.of(2023,12,23,0,0,0),
                                null,null,"Открыт",365, null,
                                LocalDate.of(2023,12,31), BigDecimal.valueOf(12.0),
                                BigDecimal.valueOf(1.7), "ПОВЫШ",
                                BigDecimal.valueOf(12.0), "1.2", "ПОНИЖ",
                                BigDecimal.valueOf(15.4), "1.4", "ПОВЫШ"
                        ),
                        new InstanceArrangment(
                                null,null, "СМО",35787,
                                "18/3", LocalDateTime.of(2024,1,12,0,0,0),
                                null,null,"Открыт",90, null,
                                LocalDate.of(2024,1,13), BigDecimal.valueOf(13.4),
                                BigDecimal.valueOf(1.5), "ПОНИЖ",
                                BigDecimal.valueOf(12.8), "1.1", "ПОНИЖ",
                                BigDecimal.valueOf(14.9), "1.2", "ПОВЫШ"
                        )
                )
        );
        return req;
    }

    // Тело запроса на создание продуктового регистра для существующего договора
    public static ProdRegCreateRequestBody crProdRegCrReqBod(){
        ProdRegCreateRequestBody req = new ProdRegCreateRequestBody();
        req.setRegistryTypeCode("02.001.005_45344_CoPalFF");
        req.setBranchCode("0021");
        req.setCurrencyCode("500");
        req.setMdmCode("13");
        req.setPriorityCode("00");
        req.setSalesCode("50014");
        return req;
    }

    // создать тестовый продукт
    public static Product createProduct(){
        Product prod = new Product();
        prod.setId(null);
        prod.setType(ProdType.ДОГОВОР);
        prod.setProductClass(null);
        prod.setClientId("13");
        prod.setNumber("123/456");
        prod.setDateOfConclusion(LocalDate.of(2019,12,25));
        prod.setPriority("00");
        prod.setPenaltyRate(BigDecimal.valueOf(11.34));
        prod.setNso(BigDecimal.valueOf(10500.00));
        prod.setThresholdAmount(BigDecimal.valueOf(15000.00));
        prod.setRequisiteType("Имеются ограничения");
        prod.setInterestRateType("Обычная");
        prod.setTaxRate(BigDecimal.valueOf(15.33));
        prod.setAdditionalPropertiesVip(null);
        return prod;
    }


}
