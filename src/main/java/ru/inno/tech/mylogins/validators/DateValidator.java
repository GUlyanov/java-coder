package ru.inno.tech.mylogins.validators;

import org.springframework.stereotype.Component;
import ru.inno.tech.mylogins.entity.Login;
import ru.inno.tech.mylogins.entity.Model;
import ru.inno.tech.mylogins.entity.User;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DateValidator implements ModelValidator {
    ModelValidator nextValidator;

    @Override
    public boolean validate(Model model) {
        List<Login> loginList = model.getLoginList();
        for (Login login: loginList) {
            LocalDateTime dt = login.getAccessDate();
            if (dt==null) {
                User user = login.getUser();
                login.setError( "Пользователь "+user.getFio()+" ("+user.getUserName()+")."+
                                "Пустая дата и время соединения. Файл: "+login.getFileName());
            }
        }
        return true;
    }


    public ModelValidator next(){
        return nextValidator;
    }

}
