package ru.inno.tech.mylogins;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.inno.tech.mylogins.entity.Login;
import ru.inno.tech.mylogins.entity.Model;
import ru.inno.tech.mylogins.entity.User;
import ru.inno.tech.mylogins.servicies.ErrorWriter;
import ru.inno.tech.mylogins.servicies.ModelErrorWriter;
import ru.inno.tech.mylogins.validators.AppValidator;
import ru.inno.tech.mylogins.validators.DateValidator;
import ru.inno.tech.mylogins.validators.FioValidator;
import ru.inno.tech.mylogins.validators.ModelValidator;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class ValidatorUnitTests {

    @Test
    @DisplayName("Проверка модуля FioValidator")
    public void TestFioValidator(){
        File file = new File("data/file1.txt");
        User user = new User("SV_PETROV", "пеТров сеМен ВикТорович");
        List<File> listFile = List.of(file);
        List<User> listUser = List.of(user);
        Login login = new Login(null, LocalDateTime.now(),"mobile", file);
        user.addLogin(login);
        List<Login> listLogin = List.of(login);
        Model md = new Model(listLogin, listUser, listFile);
        // тест функции firstUpper
        Assertions.assertEquals("Петров", FioValidator.firstUpper("пеТров"), "Функция firstUpper");

        String oldFio = md.getUserList().get(0).getFio();
        Assertions.assertEquals("Петров Семен Викторович", FioValidator.handString(oldFio), "Функция handString");

        ModelValidator ff = new FioValidator();
        ff.validate(md);
        String newFio = md.getUserList().get(0).getFio();
        //String newFio = user.getFio();
        Assertions.assertEquals("Петров Семен Викторович", newFio, "ФИО преобразовано");
    }

    @Test
    @DisplayName("Проверка модуля DateValidator")
    public void TestDateValidator(){
        File file = new File("data/file2.txt");
        User user = new User("SV_PETROV", "Петров Семен Викторович");
        List<File> listFile = List.of(file);
        List<User> listUser = List.of(user);
        Login login = new Login(null, null, "mobile", file); // пустая дата-время
        user.addLogin(login);
        List<Login> listLogin = List.of(login);
        Model md = new Model(listLogin, listUser, listFile);

        ModelValidator ff = new DateValidator();
        ff.validate(md);
        ModelErrorWriter errorWriter = new ErrorWriter();
        String folder = "data";
        errorWriter.writeErrors(folder, md);
        File fileError = new File(folder+"/errors.bad");
        String sError = "нет ошибки";
        try(Scanner sc = new Scanner(fileError)) {
            while(sc.hasNextLine()){
                sError = sc.nextLine().trim();
                break;
            }
        } catch(IOException e){
            e.printStackTrace();
        }

        Assertions.assertEquals("Пользователь Петров Семен Викторович (SV_PETROV)."+
                "Пустая дата и время соединения. Файл: file2.txt", sError, "Ошибка пустая дата из журнала");
    }

    @Test
    @DisplayName("Проверка модуля AppValidator")
    public void TestAppValidator(){
        File file = new File("data/file1.txt");
        User user = new User("SV_PETROV", "Петров Семен Викторович");
        List<File> listFile = List.of(file);
        List<User> listUser = List.of(user);
        Login login1 = new Login(null, LocalDateTime.now(),"mobile", file);
        Login login2 = new Login(null, LocalDateTime.now(),"web", file);
        Login login3 = new Login(null, LocalDateTime.now(),"post", file);
        user.addLogin(login1);
        user.addLogin(login2);
        user.addLogin(login3);
        List<Login> listLogin = List.of(login1,login2,login3);
        Model md = new Model(listLogin, listUser, listFile);

        ModelValidator ff = new AppValidator();
        ff.validate(md);
        String newFio = md.getUserList().get(0).getFio();
        //String newFio = user.getFio();
        Assertions.assertEquals("mobile", login1.getApplication(), "mobile без изменений");
        Assertions.assertEquals("web", login2.getApplication(), "web без изменений");
        Assertions.assertEquals("other: post", login3.getApplication(), "прочие с приставкой other:");
    }



}
