package ru.inno.tech.products.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.inno.tech.products.entities.ProductRegister;
import ru.inno.tech.products.requests.ProdRegCreateRequestBody;
import ru.inno.tech.products.requests.ProdRegCreateResponseBody;
import ru.inno.tech.products.servicies.ProductService;
import ru.inno.tech.products.servicies.RegisterService;
import ru.inno.tech.products.servicies.RegisterServiceInt;

@RestController
public class RegisterController {
    RegisterService regServ;

    public RegisterController(RegisterService regServ) {
        this.regServ = regServ;
    }

    // Точка. Создание продуктового регистра и получение и привязка счета к нему
    @PostMapping("/corporate-settlement-account/create")
    public ResponseEntity<ProdRegCreateResponseBody> createProductRegister(
            @RequestBody @Valid ProdRegCreateRequestBody reqBody
    ) {
        // 1.Проверка тела запроса на обязательные поля - при передаче параметра метода

        // 2.Формирование продуктового регистра  в памяти
        ProductRegister prodReg = regServ.formProdReg(reqBody);

        // 3.Обработка договора Product -
        regServ.handProdReg(prodReg, reqBody);

        // 4.Формирование ответа на запрос
        ProdRegCreateResponseBody respBody = regServ.formProdRegResponse(prodReg);

        // 5.Возврат результата
        return ResponseEntity
                .status(200)
                .body(respBody);
    }


}
