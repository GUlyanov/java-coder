package ru.inno.tech.mylogins.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.inno.tech.mylogins.aspects.LogTransformation;
import ru.inno.tech.mylogins.entity.Model;
import ru.inno.tech.mylogins.entity.User;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@LogTransformation(fileName = "data/validators.log")
public class FioValidator implements ModelValidator {
    @Autowired
    @Qualifier("appValidator")
    ModelValidator nextValidator;

    @Override
    public boolean validate(Model model) {
        List<User> userList = model.getUserList();
        for (User user: userList)
            user.setFio(handString(user.getFio()));
        return true;
    }

    public ModelValidator next(){
        return nextValidator;
    }

    public static String handString(String str){
        String[] words = str.split(" ");
        String rez = Arrays.stream(words)
                            .map(x->firstUpper(x))
                            .collect(Collectors.joining(" "));
        return rez;
    }
    public static String firstUpper(String str){
        String newStr = Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
        return newStr;
    }
}
