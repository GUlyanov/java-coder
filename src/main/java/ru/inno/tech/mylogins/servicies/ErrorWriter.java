package ru.inno.tech.mylogins.servicies;

import org.springframework.stereotype.Component;
import ru.inno.tech.mylogins.entity.Login;
import ru.inno.tech.mylogins.entity.Model;

import java.io.FileWriter;
import java.io.IOException;

@Component
public class ErrorWriter implements ModelErrorWriter{
    @Override
    public void writeErrors(String folderName, Model model) {
        String fileName = folderName + "/errors.bad";
        try(FileWriter writer = new FileWriter(fileName, false))
        {
            for (Login login : model.getLoginList()) {
                String sError = login.getError();
                if (sError!=null && !sError.isEmpty()) {
                    writer.write(login.getError());
                    writer.flush();
                }
            }
        }
        catch(IOException ex){ ex.getStackTrace(); }
    }
}
