package ru.inno.tech.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.inno.tech.products.controllers.ProductController;
import ru.inno.tech.products.controllers.RegisterController;
import ru.inno.tech.products.entities.AccountType;
import ru.inno.tech.products.entities.Product;
import ru.inno.tech.products.requests.ProdRegCreateRequestBody;
import ru.inno.tech.products.requests.ProdRegCreateResponseBody;
import ru.inno.tech.products.servicies.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegisterController.class)
@ComponentScan
public class UnitRegControllerTests {
    @MockBean
    RegisterService regServ;
    @MockBean
    ProductService prodServ;
    @MockBean
    AgreementService agrServ;
    @MockBean
    AccountPoolService accPoolServ;
    @MockBean
    AccountTypeService accTypeServ;
    @MockBean
    ProductClassService prodClServ;
    @MockBean
    ProductRegisterTypeService regTypeServ;

    @Autowired
    private MockMvc mockMvc;
    public ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void createMapper(){
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("1.3.Проверка создания доп ПР к существующему договору")
    public void testCreateProdReg() throws Exception {
        // 1 Создать тестовый запрос на создание доп ПР к существующему договору
        ProdRegCreateRequestBody req = TestDataCreator.crProdRegCrReqBod();
        req.setInstanceId(1);
        String reqStr = mapper.writeValueAsString(req);
        ProdRegCreateResponseBody res = new ProdRegCreateResponseBody(3);
        when(regServ.formProdRegResponse(any())).thenReturn(res);
        Product prod = TestDataCreator.createProduct();
        when(prodServ.findProductById(0, false)).thenReturn(prod);
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
