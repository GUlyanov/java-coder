package ru.inno.tech.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.inno.tech.products.controllers.ProductController;
import ru.inno.tech.products.requests.ProdRegCreateRequestBody;
import ru.inno.tech.products.requests.ProdRegCreateResponseBody;
import ru.inno.tech.products.requests.ProductCreateRequestBody;
import ru.inno.tech.products.requests.ProductCreateResponseBody;
import ru.inno.tech.products.servicies.ProductService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class UnitControllerTests {
    @MockBean
    ProductService prodServ;
    @Autowired
    private MockMvc mockMvc;
    public ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void createMapper(){
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("1.1.Проверка создания договора")
    public void testCreateProduct() throws Exception {
        // 1 Создать тестовый запрос на создание договора
        ProductCreateRequestBody req = TestDataCreator.crProdCrReqBod();
        String reqStr = mapper.writeValueAsString(req);
        ProductCreateResponseBody res = new ProductCreateResponseBody(1,  null, List.of(1,2));
        when(prodServ.formProdResponse(any())).thenReturn(res);
        // 2 Передать запрос на создание договора
        ResultActions resAct = mockMvc.perform(post("/corporate-settlement-instance/create")
                .content(reqStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        // 3 проверка содержимого ответа на запрос
        var expected = mapper.writeValueAsString(res);
        resAct.andExpect(content().json(expected));
    }

    @Test
    @DisplayName("1.2.Проверка создания доп соглашений к существующему договору")
    public void testCreateAgreements() throws Exception {
        // 1 Создать тестовый запрос на создание доп соглашений к существующему договору
        ProductCreateRequestBody req = TestDataCreator.crAgrsCrReqBod();
        req.setInstanceId(1);
        String reqStr = mapper.writeValueAsString(req);
        ProductCreateResponseBody res = new ProductCreateResponseBody(1,  List.of(1,2), List.of(1,2));
        when(prodServ.formProdResponse(any())).thenReturn(res);
        // 2 Передать запрос на создание договора
        ResultActions resAct = mockMvc.perform(post("/corporate-settlement-instance/create")
                        .content(reqStr)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        // 3 проверка содержимого ответа на запрос
        var expected = mapper.writeValueAsString(res);
        resAct.andExpect(content().json(expected));
    }

    @Test
    @DisplayName("1.3.Проверка создания доп ПР к существующему договору")
    public void testCreateProdReg() throws Exception {
        // 1 Создать тестовый запрос на создание доп ПР к существующему договору
        ProdRegCreateRequestBody req = TestDataCreator.crProdRegCrReqBod();
        req.setInstanceId(1);
        String reqStr = mapper.writeValueAsString(req);
        ProdRegCreateResponseBody res = new ProdRegCreateResponseBody(3);
        when(prodServ.formProdRegResponse(any())).thenReturn(res);
        // 2 Передать запрос на создание договора
        ResultActions resAct = mockMvc.perform(post("/corporate-settlement-account/create")
                        .content(reqStr)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        // 3 проверка содержимого ответа на запрос
        var expected = mapper.writeValueAsString(res);
        resAct.andExpect(content().json(expected));
    }

}
