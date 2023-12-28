package ru.inno.tech.mylogins.servicies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.inno.tech.mylogins.entity.ConnectInfo;
import ru.inno.tech.mylogins.entity.Login;
import ru.inno.tech.mylogins.entity.Model;
import ru.inno.tech.mylogins.entity.User;
import ru.inno.tech.mylogins.repositories.LoginRepository;
import ru.inno.tech.mylogins.repositories.UserRepository;

import javax.sql.DataSource;

@Component
public class LoginSaver implements ModelConsumer{
    private final LoginRepository loginRepository;
    private final UserRepository userRepository;

    //@Autowired
    //private DataSource dataSource;

    public LoginSaver(LoginRepository loginRepository,
                      UserRepository userRepository) {
        this.loginRepository = loginRepository;
        this.userRepository = userRepository;
    }

    public void save(ConnectInfo connInfo, Model model){
        //HikariDataSource x = (HikariDataSource)dataSource;
        //x.setJdbcUrl(connInfo.getConnStr());
        //x.setUsername(connInfo.getUserName());
        //x.setPassword(connInfo.getPassword());

        // Почистим таблицы, если там что то есть
        loginRepository.deleteAll();
        userRepository.deleteAll();

        // Запись логинов в базу
        for (Login login : model.getLoginList()){
            // Записываем в базу те логины, которые не имеют ошибок после проверок (например, дата не пустая)
            if(login.getId()==null && login.getError()==null) {
                loginRepository.save(login);
            }
        }
    }
}
