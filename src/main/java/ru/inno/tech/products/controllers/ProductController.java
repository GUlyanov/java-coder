package ru.inno.tech.products.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import ru.inno.tech.products.entities.Agreement;
import ru.inno.tech.products.entities.Product;
import ru.inno.tech.products.entities.ProductRegister;
import ru.inno.tech.products.requests.ProdRegCreateRequestBody;
import ru.inno.tech.products.requests.ProdRegCreateResponseBody;
import ru.inno.tech.products.requests.ProductCreateRequestBody;
import ru.inno.tech.products.requests.ProductCreateResponseBody;
import ru.inno.tech.products.servicies.AgreementService;
import ru.inno.tech.products.servicies.ProductService;
import ru.inno.tech.products.servicies.ProductServiceInt;

import java.util.Set;


@RestController
public class ProductController {
    @Autowired
    ProductService prodServ;
    @Autowired
    AgreementService agrServ;

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
        Set<Agreement> agrSet = agrServ.formAgreements(reqBody);

        // 3.Обработка договора Product -
        prodServ.handProduct(product, agrSet, reqBody);

        // 4.Формирование ответа на запрос
        ProductCreateResponseBody respBody = prodServ.formProdResponse(product);

        // 5.Возврат результата
        return ResponseEntity
                .status(200)
                .body(respBody);
    }


}
