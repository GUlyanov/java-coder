package ru.inno.tech.mylogins;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.inno.tech.mylogins.entity.Login;
import ru.inno.tech.mylogins.entity.Model;
import ru.inno.tech.mylogins.entity.User;
import ru.inno.tech.mylogins.validators.FioValidator;
import ru.inno.tech.mylogins.validators.ModelValidator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;


@EnableAspectJAutoProxy
@SpringBootTest
public class AopIntegrTest {
    @Autowired
    @Qualifier("fioValidator")
    ModelValidator validator;

    @Test
    @DisplayName("Логирование промежуточных компонент")
    public void TestLogComp(){
        //
        Assertions.assertNotNull(validator, "1.Не подключился первый валидатор");
        Assertions.assertTrue(validator instanceof FioValidator, "2.Подключился не тот валидатор: ");

        // Удалим старый лог, если существует
        deleteLogs("data");
        Path path = Path.of("data/validators.log");
        Assertions.assertThrows(NoSuchFileException.class, ()->Files.readAllLines(path), "3.Лог валидаторов не удален");

        // наполним модель данных в памяти
        File file = new File("data/file1.txt");
        User user1 = new User("SV_PETROV", "Петров Семен Викторович");
        User user2 = new User("DI_EGOROV", "Егоров Дормидонт Ильич");
        List<File> listFile = List.of(file);
        List<User> listUser = List.of(user1,user2);
        Login login1 = new Login(null, LocalDateTime.of(2023, 12, 23, 22, 54, 21),"mobile", file);
        Login login2 = new Login(null, LocalDateTime.of(2023, 11, 5, 10, 22, 51),"web", file);
        Login login3 = new Login(null, LocalDateTime.of(2023, 10, 15, 17, 31, 18),"post", file);
        user1.addLogin(login1);
        user1.addLogin(login2);
        user2.addLogin(login3);
        List<Login> listLogin = List.of(login1,login2,login3);
        Model model = new Model(listLogin, listUser, listFile);

        // выполнение валидаторов
        while (validator!=null){
            // проверить порцию данных
            boolean rez = validator.validate(model);
            if (rez==false) break;
            validator = validator.next();
        }

        try {
            // проверка наличия файла логирования запуска валидаторов
            Assertions.assertDoesNotThrow(() -> Files.readAllLines(path), "4.Лог валидатора не читается");
            // проверка содержимого файлов логирования запуска валидаторов
            List<String> appLst = Files.readAllLines(path);
            Assertions.assertEquals(12, appLst.size(), "В файле валидаторов неверное число строк");
            Assertions.assertTrue(containStr(appLst,"AppValidator.validate"), "не запускался валидатор приложений, хотя включен");
            Assertions.assertTrue(containStr(appLst,"FioValidator.validate"), "не запускался валидатор ФИОб хотя включен");
            Assertions.assertFalse(containStr(appLst,"DateValidator.validate"), "запускался валидатор дат, хотя не включен");
        } catch (IOException e){e.getStackTrace();}
    }

    // Очистка файлов журналов в папке (типа .log)
    public void deleteLogs (String dirName){
        File fl = new File(dirName);
        File[] ff = fl.listFiles();
        for (int i = 0; i < ff.length; i++) {
            if (ff[i].isFile() && ff[i].getName().endsWith(".log"))
                ff[i].delete();
        }
    }

    public boolean containStr(List<String> lst, String str){
        return lst.stream().anyMatch((x)->x.contains(str));
    }

}
