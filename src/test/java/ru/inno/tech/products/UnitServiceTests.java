package ru.inno.tech.products;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.inno.tech.products.entities.*;
import ru.inno.tech.products.repositories.*;
import ru.inno.tech.products.requests.ProductCreateRequestBody;
import ru.inno.tech.products.requests.ProductCreateResponseBody;
import ru.inno.tech.products.servicies.ProductService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UnitServiceTests {
    @Mock
    ProductClassRepository prodClRep;
    @Mock
    ProductRepository prodRep;
    @Mock
    AccountTypeRepository accTypeRep;
    @Mock
    ProductRegisterTypeRepository prodRegTypeRep;
    @Mock
    ProductRegisterRepository prodRegRep;
    @Mock
    AccountPoolRepository accPoolRep;
    @InjectMocks
    ProductService prodServ;
    @Test
    @DisplayName("Формирование запроса на создание договора")
    public void testFormProduct(){
        // 1 Создать тестовый запрос на создание договора
        ProductCreateRequestBody reqBody = TestDataCreator.crProdCrReqBod();
        // 2 Указания для методов мокированной зависимости
        ProductClass prodCl = new ProductClass(1, reqBody.getProductCode());
        when(prodClRep.findProductClassByValue(reqBody.getProductCode())).thenReturn(Optional.of(prodCl));
        // 3 Вызвать метод сервиса
        Product product = prodServ.formProduct(reqBody);
        // 4 Проверить результат
        Assertions.assertEquals(reqBody.getContractNumber(), product.getNumber(), "Номер договора в запросе и договоре различаются");
        Assertions.assertEquals(reqBody.getPriority(), product.getPriority(), "Приоритет в запросе и договоре различаются");
    }

    @Test
    @DisplayName("Создание договора")
    public void testHandProduct(){
        // 1 Подготовка к тесту: создать тестовый объект договор
        ProductCreateRequestBody reqBody = TestDataCreator.crProdCrReqBod();
        ProductClass prodCl = new ProductClass(1, reqBody.getProductCode());
        when(prodClRep.findProductClassByValue(reqBody.getProductCode())).thenReturn(Optional.of(prodCl));
        Product product = prodServ.formProduct(reqBody);

        // 2 Найти типы регистра для заданного в запросе класса продукта
        AccountType accTp = new AccountType(1, "Клиентский");
        Set<ProductRegisterType> regTypes = new HashSet<>();
        regTypes.add(new ProductRegisterType(1,"02.001.005_45343_CoDowFF"));
        regTypes.add(new ProductRegisterType(2,"02.001.005_45344_CoPalFF"));
        ProductRegisterType regType = regTypes.stream().filter(p->p.getValue().equals("02.001.005_45344_CoPalFF")).findFirst().get();
        AccountPool accPool = new AccountPool(2, "0021", "500", "13", "00", "02.001.005_45343_CoDowFF", "453432352436453276");

        // 3 Указания для методов мокированных зависимостей
        when(prodRep.findProductByNumber(product.getNumber())).thenReturn(Optional.empty());  // в базе договор еще не создан
        when(prodRep.save(any())).then(returnsFirstArg());
        when(accTypeRep.findAccountTypeByValue("Клиентский")).thenReturn(Optional.of(accTp));
        when(prodRegTypeRep.findProductRegisterTypeByProductClassAndAccountType(product.getProductClass(), accTp)).thenReturn(regTypes);
        when(prodRegRep.findProductRegisterByProductAndRegisterType(any(), any())).thenReturn(Optional.empty()); // в базе у договора нет регистра данного типа
        when(prodRegRep.save(any())).then(returnsFirstArg());
        when(accPoolRep.getAccountPool(accPool.getBranchCode(), accPool.getCurrencyCode(), accPool.getMdmCode(), accPool.getPriority(), accPool.getRegistryTypeCode()))
                .thenReturn(Optional.of(accPool));

        // 3 Вызвать метод сервиса
        prodServ.handProduct(product, null, reqBody);
        //
        // 4 Проверить результат
        Assertions.assertEquals(reqBody.getContractNumber(), product.getNumber(), "Номер договора в запросе и договоре различаются");
        Assertions.assertEquals(reqBody.getPriority(), product.getPriority(), "Приоритет в запросе и договоре различаются");
        Assertions.assertEquals(2, product.getRegisters().size(), "У договора должно быть два продуктовых регистра");
        Assertions.assertEquals(1, regType.getRegisters().size(), "У типа регистра должен быть один продуктовый регистр");
    }
    @Test
    @DisplayName("Формирование ответа на запрос создания договора")
    public void testFormResponse() {
        // 1 Подготовка к тесту: создать тестовый объект договор
        ProductCreateRequestBody reqBody = TestDataCreator.crProdCrReqBod();
        ProductClass prodCl = new ProductClass(1, reqBody.getProductCode());
        when(prodClRep.findProductClassByValue(reqBody.getProductCode())).thenReturn(Optional.of(prodCl));
        Product product = prodServ.formProduct(reqBody);
        // 2 Вызвать метод сервиса
        ProductCreateResponseBody respBody = prodServ.formProdResponse(product);
        // 3 Проверить результат
        Assertions.assertEquals(product.getId(), respBody.getInstanceId(),"Ид договора в ответе и договоре различаются");
        Assertions.assertEquals(product.getRegisters().size(), respBody.getRegisterId().size(), "Количество Ид продуктовых регистров в ответе и договоре различаются");
    }
}
