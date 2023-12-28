package ru.inno.tech.mylogins;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.inno.tech.mylogins.entity.Login;
import ru.inno.tech.mylogins.entity.Model;
import ru.inno.tech.mylogins.servicies.LoginReader;
import ru.inno.tech.mylogins.servicies.ModelSupplier;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import org.junit.jupiter.api.Assertions;

public class LoginReaderUnitTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginReaderUnitTest.class);

    @Test
    @DisplayName("Проверка модуля LoginReader")
    public void TestLoginReader() {
        ModelSupplier supplier = new LoginReader();
        String folderName = "data";
        Model model = supplier.read(folderName);
        Assertions.assertEquals(4, model.getLoginList().size(), "1.Неверное число логинов считано из файла");
        Assertions.assertEquals(4, model.getUserList().size(), "2.Неверное число пользователей считано из файла");
        LOGGER.info("1.Список логинов, считанных из файла:");
        model.getLoginList().forEach(i -> LOGGER.info(i.toString()));
        LOGGER.info("2.Список пользователей, считанных из файла:");
        model.getUserList().forEach(i -> LOGGER.info(i.toString()));
    }

}
