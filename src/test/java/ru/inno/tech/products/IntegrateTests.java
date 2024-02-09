package ru.inno.tech.products;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hibernate.annotations.processing.SQL;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.inno.tech.products.entities.Product;
import ru.inno.tech.products.entities.ProductRegister;
import ru.inno.tech.products.entities.ProductRegisterType;
import ru.inno.tech.products.requests.*;
import ru.inno.tech.products.servicies.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@EnableTransactionManagement
@AutoConfigureMockMvc
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=validate"})
@Sql(value="/clear-data.sql", executionPhase=AFTER_TEST_METHOD)
class IntegrateTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ProductServiceInt prodServ;
	@Autowired
	private RegisterServiceInt regServ;
	@Autowired
	private ProductRegisterTypeService regTypeServ;

	public ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	public void createMapper(){
		mapper.registerModule(new JavaTimeModule());
	}

	@AfterEach
	public void resetDb() {
	}

	//----------------------------------------------------------------------------------------------------
	@Test
	@DisplayName("1.1.Создать договор. Ошибка: обязательные поля в запросе не заполнены")
	void test1_1() throws Exception{
		// 1 Создать тестовый запрос на создание
		ProductCreateRequestBody req = TestDataCreator.crProdCrReqBod();
		req.setContractNumber(null); // обязательное поле
		req.setPriority(null);       // обязательное поле
		var reqStr = mapper.writeValueAsString(req);

		// 2 Выполнение запроса, получение сообщения об ошибке и статус 400
		String expected = "Обязательные параметры не заполнены: \n" + "contractNumber, priority";
		mockMvc.perform(post("/corporate-settlement-instance/create")
				.content(reqStr)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(400)) //
				.andExpect(content().string(expected));
	}

	//----------------------------------------------------------------------------------------------------
	@Test
	@DisplayName("1.2.Создать договор. Ошибка: код класса продукта не существует")
	void test1_2() throws Exception {

		// 1 Создать тестовый запрос на создание
		ProductCreateRequestBody req = TestDataCreator.crProdCrReqBod();
		req.setProductCode("XXXX"); // несуществующий код
		var reqStr = mapper.writeValueAsString(req);

		// 2 Выполнение запроса, получение сообщения об ошибке и статус 400
		String expected = "КодПродукта <XXXX> не найдено в Каталоге продуктов prodsch.TPP_REF_PRODUCT_CLASS";
		mockMvc.perform(post("/corporate-settlement-instance/create")
						.content(reqStr)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(404))
				.andExpect(content().string(expected));
	}

	//----------------------------------------------------------------------------------------------------
	@Test
	@DisplayName("1.3.Создать договор. Ошибка: договор с таким номером уже существует в базе")
	void test1_3() throws Exception {
		// 1 Создать тестовый запрос на создание договора
		ProductCreateRequestBody req = TestDataCreator.crProdCrReqBod();

		// 2 Создать в базе договор
		ResultActions resAct = sendProductCreateRequest(req);
		resAct.andExpect(status().is(200));

		// 3 Выполнение запроса повторно, получение сообщения об ошибке и статус 400
		resAct = sendProductCreateRequest(req);
		resAct.andExpect(status().is(400));

		// 4 Проверка текста сообщения об ошибке в теле
		Product product = prodServ.findProductByNumber(req.getContractNumber(), false);
		String sMess = "Параметр ContractNumber № договора <%s> уже существует для ЭП с ИД <%s>";
		sMess = sMess.formatted(product.getNumber(), product.getId());
		resAct.andExpect(content().string(sMess));
	}

	//----------------------------------------------------------------------------------------------------
	@Test
	@DisplayName("1.4.Создать договор. Успешно")
	void test1_4() throws Exception {
		// 1 Создать тестовый запрос на создание
		ProductCreateRequestBody req = TestDataCreator.crProdCrReqBod();

		// 2 Создать договор
		ResultActions resAct = sendProductCreateRequest(req);
		resAct.andExpect(status().is(200));

		// 3 проверка создания договора в базе
		Product product = prodServ.findProductByNumber(req.getContractNumber(), false);
		Assertions.assertNotNull(product, "Не создан договор с номером "+req.getContractNumber());
		Assertions.assertNotNull(product.getId(), "Не создан договор");
		//Assertions.assertEquals(1, prodServ.findProductRegisterByProduct(product).size());
		Assertions.assertEquals(2, product.getRegisters().size(), "Не создан продуктовый регистр!");
		ProductRegisterType regType = regTypeServ.findProdRegTypeByValue("02.001.005_45343_CoDowFF", true);
		ProductRegister prodReg = regServ.findProdRegByProductAndRegType(product, regType, false);
		Assertions.assertNotNull(prodReg, "Не создан продуктовый регистр с кодом типа 02.001.005_45343_CoDowFF");
		Assertions.assertNotNull(prodReg.getAccount(), "Не получен счет для продуктового регистра договора!");
		Assertions.assertNotNull(prodReg.getAccountNumber(), "Не получен номер счета для продуктового регистра договора!");

		// 4 формирование тела ожидаемого ответа
		ProductCreateResponseBody resBody = prodServ.formProdResponse(product);
		var expected = mapper.writeValueAsString(resBody);

		// 5 проверка содержимого ответа на запрос
		resAct.andExpect(content().json(expected));
	}

	//----------------------------------------------------------------------------------------------------

	@Test
	@DisplayName("2.1.Создать допсоглашения к существующему договору. Успешно")
	void test2_1() throws Exception {
		// 1 Создать договор
		ProductCreateRequestBody req1 = TestDataCreator.crProdCrReqBod();
		ResultActions resAct = sendProductCreateRequest(req1);
		resAct.andExpect(status().is(200));

		// 2 Найти договор в базе по номеру
		Product product = prodServ.findProductByNumber("123/456", false);
		Assertions.assertNotNull(product, "Договор не открыт");
		Assertions.assertEquals(2, product.getRegisters().size(),"1-Неверное число продуктовых регистров у договора");

		// 3 Создать допосоглашения к существующему договору
		ProductCreateRequestBody req2 = TestDataCreator.crAgrsCrReqBod();
		resAct = sendAgreementCreateRequest(req2, product.getId());
		resAct.andExpect(status().is(200));

		// 4 проверка наличия допосоглашений договора в базе, а также регистров для полноты ответа
		product = prodServ.findProductByNumber("123/456", false);
		Assertions.assertNotNull(product, "Договор с номером 123/456 не найден в базе");
		Assertions.assertEquals(2, product.getAgreements().size(),"2-Неверное число допсоглашений у договора");
		Assertions.assertEquals(2, product.getRegisters().size(),"2-Неверное число продуктовых регистров у договора");

		// 5 формирование тела ожидаемого ответа
		ProductCreateResponseBody resBody = prodServ.formProdResponse(product);
		var expected = mapper.writeValueAsString(resBody);
		// 6 проверка содержимого ответа на запрос
		resAct.andExpect(content().json(expected));
	}

	@Test
	@DisplayName("2.2.Создать допсоглашения к существующему договору. Ошибка. Соглашение с таким номером у договора есть")
	void test2_2() throws Exception {
		// 1 Создать договор
		ProductCreateRequestBody req1 = TestDataCreator.crProdCrReqBod();
		ResultActions resAct = sendProductCreateRequest(req1);
		resAct.andExpect(status().is(200));

		// 2 Найти договор в базе по номеру
		Product product = prodServ.findProductByNumber("123/456", false);
		Assertions.assertNotNull(product, "Договор не открыт");

		// 4 Создать допсоглашения к договору
		ProductCreateRequestBody req2 = TestDataCreator.crAgrsCrReqBod();
		sendAgreementCreateRequest(req2, product.getId());
		product = prodServ.findProductByNumber("123/456", false);
		Assertions.assertNotNull(product, "Договор с номером 123/456 не найден в базе");
		Assertions.assertEquals(2, product.getAgreements().size(),"2-Неверное число допсоглашений у договора");

		// 5 Повторно создать допосоглашения к договору
		resAct =  sendAgreementCreateRequest(req2, product.getId());
		resAct.andExpect(status().is(400));

		// 6 проверка содержимого ответа на запрос
		String sMess = "Параметр № Дополнительного соглашения (сделки) Number <%s> уже существует для ЭП с ИД  <%s>";
		String sMess1 = sMess.formatted("15/2", product.getId());
		String sMess2 = sMess.formatted("18/3", product.getId());
		String sRez = resAct.andReturn().getResponse().getContentAsString();
		Assertions.assertTrue(sRez.equals(sMess1) ||  sRez.equals(sMess2));
	}

	//----------------------------------------------------------------------------------------------------
	@Test
	@DisplayName("3.1.Создать доп продуктовый регистр к существующему договору. Успешно")
	void test3_1() throws Exception {
		// 1 Подготовка среды для теста
		// 1.1. Создать договор
		ProductCreateRequestBody req1 = TestDataCreator.crProdCrReqBod();
		ResultActions resAct = sendProductCreateRequest(req1);
		resAct.andExpect(status().is(200));
		// 1.1.Найти договор в базе по номеру
		Product product = prodServ.findProductByNumber("123/456", false);
		// 1.2.Найти тип регистра по коду типа
		ProductRegisterType regType = regTypeServ.findProdRegTypeByValue("02.001.005_45344_CoPalFF", true);
		// 1.3 Очистить продуктовый регистр у договора, если он привязан к договору
		regServ.deleteProdRegByProdAndRegType(product, regType);
		// 1.4 Проверить, что ПР, который мы будем создавать, к началу теста отсутствует у договора
		ProductRegister prodReg = regServ.findProdRegByProductAndRegType(product, regType, false);
		Assertions.assertNull(prodReg, "1.4.Не удалось удалить продуктовый регистр");

		// 2 Создать тестовый запрос на создание дополнительного продуктового регистра к существующему договору
		ProdRegCreateRequestBody req2 = TestDataCreator.crProdRegCrReqBod();

		// 3 Создание продуктового регистра
		resAct = sendProdRegCreateRequest(req2, product.getId());
		resAct.andExpect(status().is(200));

		// 4 проверка создания продуктового регистра в базе
		prodReg = regServ.findProdRegByProductAndRegType(product, regType, false);
		Assertions.assertNotNull(prodReg, "3.Не создан продуктовый регистр");

		// 5 проверка наличия привязок продуктовых регистров и допсоглашений к нашему договору
		product = prodReg.getProduct();
		Assertions.assertEquals(0, product.getAgreements().size(), "4.Неверное число допсоглашений у продукта");
		Assertions.assertEquals(2, product.getRegisters().size(), "4.Не открыт продуктовый регистр для договора");

		// 6 Проверка наличия открытого счета у продуктового регистра
		Assertions.assertNotNull(prodReg.getAccount(), "5.Не получен счет для продуктового регистра договора!");
		Assertions.assertNotNull(prodReg.getAccountNumber(), "5.Не получен номер счета для продуктового регистра договора!");

		// 7 формирование тела ожидаемого ответа
		ProdRegCreateResponseBody resBody = regServ.formProdRegResponse(prodReg);
		var expected = mapper.writeValueAsString(resBody);

		// 8 проверка содержимого ответа на запрос
		resAct.andExpect(content().json(expected));
	}

	@Test
	@DisplayName("3.2.Создать доп продуктовый регистр к существующему договору. Ошибка. ПР с таким типом уже существует у договора")
	void test3_2() throws Exception {
		// 1 Создать договор (на этот раз мы не удаляем один из ПР договора)
		ProductCreateRequestBody req1 = TestDataCreator.crProdCrReqBod();
		ResultActions resAct = sendProductCreateRequest(req1);
		resAct.andExpect(status().is(200));

		// 2 Создать тестовый запрос на создание дополнительного продуктового регистра к существующему договору
		ProdRegCreateRequestBody req2 = TestDataCreator.crProdRegCrReqBod();
		Product product = prodServ.findProductByNumber(req1.getContractNumber(), false);

		// 3 Создание продуктового регистра
		resAct = sendProdRegCreateRequest(req2, product.getId());
		resAct.andExpect(status().is(400));

		// 4 формирование тела ожидаемого ответа
		String sMess = "Параметр registryTypeCode тип регистра <%s> уже существует для ЭП с ИД  <%s>";
		sMess = sMess.formatted(req2.getRegistryTypeCode(), product.getId());
		resAct.andExpect(content().string(sMess));
	}

	//========  Вспомогательные методы - передача запросов на создание ччерез MockMvc ===========================
	// Cоздание договора
	ResultActions sendProductCreateRequest(ProductCreateRequestBody req) throws Exception {
		var reqStr = mapper.writeValueAsString(req);

		// Передача запроса и получение ответа
		ResultActions resAct = mockMvc.perform(post("/corporate-settlement-instance/create")
										.content(reqStr)
										.contentType(MediaType.APPLICATION_JSON));
		return resAct;
	}

	// Cоздание допсоглашений к существующему договору c id
	ResultActions sendAgreementCreateRequest(ProductCreateRequestBody req, Integer id) throws Exception {
		req.setInstanceId(id);
		var reqStr = mapper.writeValueAsString(req);

		// передача запроса и получение ответа
		ResultActions resAct = mockMvc.perform(post("/corporate-settlement-instance/create")
										.content(reqStr)
										.contentType(MediaType.APPLICATION_JSON));
		return resAct;
	}

	// Cоздание доп продуктового регистра к существующему договору c id
	ResultActions sendProdRegCreateRequest(ProdRegCreateRequestBody req, Integer id) throws Exception {
		req.setInstanceId(id);
		var reqStr = mapper.writeValueAsString(req);

		// передача запроса и получение ответа
		ResultActions resAct = mockMvc.perform(post("/corporate-settlement-account/create")
				.content(reqStr)
				.contentType(MediaType.APPLICATION_JSON));
		return resAct;
	}


}

