package ru.inno.tech.products;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.inno.tech.products.entities.Product;
import ru.inno.tech.products.entities.ProductClass;
import ru.inno.tech.products.repositories.*;

import java.util.Optional;

@DataJpaTest
//@SpringBootTest
public class UnitRepositoryTests {
    @Autowired
    ProductClassRepository prodClRep;
    @Autowired
    ProductRepository prodRep;
    /*
    @Autowired
    AccountTypeRepository accTypeRep;
    @Autowired
    ProductRegisterTypeRepository prodRegTypeRep;
    @Autowired
    ProductRegisterRepository prodRegRep;
    @Autowired
    AccountPoolRepository accPoolRep; */

    @Test
    @DisplayName("Проверка репозитория продуктов")
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

}
