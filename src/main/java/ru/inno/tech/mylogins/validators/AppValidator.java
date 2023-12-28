package ru.inno.tech.mylogins.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.inno.tech.mylogins.aspects.LogTransformation;
import ru.inno.tech.mylogins.entity.Login;
import ru.inno.tech.mylogins.entity.Model;

import java.util.List;

@Component
@LogTransformation(fileName = "data/validators.log")
public class AppValidator implements ModelValidator {
    @Autowired
    @Qualifier("dateValidator")
    ModelValidator nextValidator;

    @Override
    public boolean validate(Model model) {
        List<Login> loginList = model.getLoginList();
        for (Login login: loginList) {
            String s = login.getApplication();
            if (s.equals("mobile") || s.equals("web")) continue;
            s = "other: " + s;
            login.setApplication(s);
        }
        return true;
    }

    public ModelValidator next(){
        return nextValidator;
    }

}
