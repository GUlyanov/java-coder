package ru.inno.tech.mylogins;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.inno.tech.mylogins.entity.Login;
import ru.inno.tech.mylogins.entity.User;
import ru.inno.tech.mylogins.repositories.LoginRepository;
import ru.inno.tech.mylogins.repositories.UserRepository;
import ru.inno.tech.mylogins.servicies.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;



@SpringBootTest
class ApplicationIntegrTests {
    @Autowired
    Controller cont;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    void Test1() throws IOException {
        cont.setFolderName("data/");
        cont.make();
        List<Login> lsLogin = loginRepository.findAll();
        List<User> lsUser = userRepository.findAll();
        Assertions.assertEquals(3,lsLogin.size(),"1.В таблице логинов неверное число записей");
        Assertions.assertEquals(3,lsUser.size(),"2.В таблице пользователей неверное число записей");
        // В файле с логинами имеющими ошибки 1 логин
        Path path = Path.of("data/errors.bad");
        Assertions.assertDoesNotThrow(()->Files.readAllLines(path),"3.Файл с ошибочными логинами не существует");
        List<String> lsBadLogin = Files.readAllLines(path);
        Assertions.assertEquals(1,lsBadLogin.size(),"4.В файле логинов с ошибками неверное число записей");
    }

}
