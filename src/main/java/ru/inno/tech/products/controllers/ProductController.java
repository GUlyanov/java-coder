package ru.inno.tech.products.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import ru.inno.tech.products.entities.Agreement;
import ru.inno.tech.products.entities.Product;
import ru.inno.tech.products.entities.ProductRegister;
import ru.inno.tech.products.requests.ProdRegCreateRequestBody;
import ru.inno.tech.products.requests.ProdRegCreateResponseBody;
import ru.inno.tech.products.requests.ProductCreateRequestBody;
import ru.inno.tech.products.requests.ProductCreateResponseBody;
import ru.inno.tech.products.servicies.ProductService;

import java.util.Set;


@RestController
public class ProductController {
    ProductService prodServ;

    //private static Logger logger = Logger.getLogger(ProductController.class.getName());

    public ProductController(ProductService productService) {
        this.prodServ = productService;
    }
    // проверка сервера
    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }

    // Точка. Создание договора
    @PostMapping("/corporate-settlement-instance/create")
    public ResponseEntity<ProductCreateResponseBody> createProduct(
            @RequestBody @Valid ProductCreateRequestBody reqBody
        ) {
        // 1.Проверка тела запроса на обязательные поля - при передаче параметра метода

        // 2.Формирование договора и допсоглашений в памяти
        Product product = prodServ.formProduct(reqBody);
        Set<Agreement> agrSet = prodServ.formAgreements(reqBody);

        // 3.Обработка договора Product -
        prodServ.handProduct(product, agrSet, reqBody);

        // 4.Формирование ответа на запрос
        ProductCreateResponseBody respBody = prodServ.formProdResponse(product);

        // 5.Возврат результата
        return ResponseEntity
                .status(200)
                .body(respBody);
    }

    // Точка. Создание продуктового регистра и получение и привязка счета к нему
    @PostMapping("/corporate-settlement-account/create")
    public ResponseEntity<ProdRegCreateResponseBody> createProductRegister(
            @RequestBody @Valid ProdRegCreateRequestBody reqBody
    ) {
        // 1.Проверка тела запроса на обязательные поля - при передаче параметра метода

        // 2.Формирование продуктового регистра  в памяти
        ProductRegister prodReg = prodServ.formProdReg(reqBody);

        // 3.Обработка договора Product -
        prodServ.handProdReg(prodReg, reqBody);

        // 4.Формирование ответа на запрос
        ProdRegCreateResponseBody respBody = prodServ.formProdRegResponse(prodReg);

        // 5.Возврат результата
        return ResponseEntity
                .status(200)
                .body(respBody);
    }


}
