package ru.inno.tech.products;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.inno.tech.products.entities.Product;
import ru.inno.tech.products.entities.ProductClass;
import ru.inno.tech.products.entities.ProductRegister;
import ru.inno.tech.products.entities.ProductRegisterType;
import ru.inno.tech.products.repositories.*;

import java.util.Optional;

@DataJpaTest
//@SpringBootTest
public class UnitRepositoryTests {
    @Autowired
    ProductClassRepository prodClRep;
    @Autowired
    ProductRepository prodRep;
    @Autowired
    ProductRegisterTypeRepository prodRegTypeRep;
    @Autowired
    ProductRegisterRepository prodRegRep;
    /*
    @Autowired
    AccountTypeRepository accTypeRep;
    @Autowired
    AccountPoolRepository accPoolRep; */

    @Test
    @DisplayName("1.Проверка репозитория продуктов")
    public void testRepoProduct(){
        // 1 Создать тестовый запрос на создание договора
        Product prod = TestDataCreator.createProduct();
        ProductClass prodCl = prodClRep.findProductClassByValue("02.001.005").get();
        prod.setProductClass(prodCl);
        // 3 Вызвать метод репозитория
        prodRep.save(prod);
        // 4 Проверить результат
        Assertions.assertNotNull(prod.getId(), "Договор не получил ид при сохранении");
        Optional<Product> prodOp = prodRep.findProductByNumber(prod.getNumber());
        Assertions.assertTrue(prodOp.isPresent(), "Договора с нужным номером нет в базе");
        Product prodDb = prodOp.get();
        Assertions.assertEquals(prod.getClientId(), prodDb.getClientId(), "Ид клиента в базе и в договоре различаются");
    }
    @Test
    @DisplayName("2.Проверка репозитория продуктового регистра")
    public void testRepoProdReg(){
        // 1 Создать договор в базе
        Product product = TestDataCreator.createProduct();
        ProductClass prodCl = prodClRep.findProductClassByValue("02.001.005").get();
        product.setProductClass(prodCl);
        prodRep.save(product);

        // 2 Создать объект типа ПР
        ProductRegister prodReg = TestDataCreator.createProductRegister();
        ProductRegisterType regType = prodRegTypeRep.findProductRegisterTypeByValue("02.001.005_45344_CoPalFF").get();
        prodReg.setRegisterType(regType);
        // 3 Вызвать метод репозитория для сохранения ПР в базу
        prodRegRep.save(prodReg);
        product.addProductRegister(prodReg);
        // 4 Проверить результат
        Assertions.assertNotNull(prodReg.getId(), "ПР не получил ид при сохранении");
        Optional<ProductRegister> prodRegOp = prodRegRep.findProductRegisterByProductAndRegisterType(product, regType);
        Assertions.assertTrue(prodRegOp.isPresent(), "ПР с нужным типом нет у договора с номером 123/456 в базе");
        ProductRegister prodRegDb = prodRegOp.get();
        Assertions.assertEquals(prodReg.getAccountNumber(), prodRegDb.getAccountNumber(), "Номер счета ПР в базе и в классе различаются");
    }

}
