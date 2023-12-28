package ru.inno.tech.mylogins;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.inno.tech.mylogins.entity.ConnectInfo;
import ru.inno.tech.mylogins.entity.Login;
import ru.inno.tech.mylogins.entity.Model;
import ru.inno.tech.mylogins.entity.User;
import ru.inno.tech.mylogins.repositories.LoginRepository;
import ru.inno.tech.mylogins.repositories.UserRepository;
import ru.inno.tech.mylogins.servicies.LoginSaver;
import ru.inno.tech.mylogins.servicies.ModelConsumer;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class LoginSaverUnitTest {
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Проверка модуля LoginSaver")
    public void TestLoginSaver(){
        File file = new File("data/file1.txt");
        User user1 = new User("SV_PETROV", "Петров Семен Викторович");
        User user2 = new User("DI_EGOROV", "Егоров Дормидонт Ильич");
        List<File> listFile = List.of(file);
        List<User> listUser = List.of(user1,user2);
        Login login1 = new Login(null, LocalDateTime.of(2023, 12, 23, 22, 54, 21),
                "mobile", file);
        Login login2 = new Login(null, LocalDateTime.of(2023, 11, 5, 10, 22, 51),
                "web", file);
        Login login3 = new Login(null, LocalDateTime.of(2023, 10, 15, 17, 31, 18),
                "post", file);
        user1.addLogin(login1);
        user1.addLogin(login2);
        user2.addLogin(login3);
        List<Login> listLogin = List.of(login1,login2,login3);
        Model model = new Model(listLogin, listUser, listFile);

        ModelConsumer consumer = new LoginSaver(loginRepository, userRepository);
        String connStr = "jdbc:postgresql://localhost:5430/postgres?currentSchema=mylogin";
        ConnectInfo connectInfo = new ConnectInfo(connStr, "postgres", "postgres");
        consumer.save(connectInfo, model);

        User user1_ = userRepository.findById(user1.getId()).get();
        Assertions.assertNotNull(user1_, "Пользователь1 сохранен  в базе");
        Assertions.assertEquals(user1.getUserName(), user1_.getUserName(), "Пользователь1 имеет в базе то же имя");
        Login login1_ = loginRepository.findById(login1.getId()).get();
        Assertions.assertNotNull(login1_, "Логин1 сохранен  в базе");
        Assertions.assertEquals(login1_.getUser().getUserName(), login1.getUser().getUserName(), "Логин1 имеет в базе то же имя пользователя");
    }

}
