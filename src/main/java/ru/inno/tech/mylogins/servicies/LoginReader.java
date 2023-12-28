package ru.inno.tech.mylogins.servicies;

import org.springframework.stereotype.Component;
import ru.inno.tech.mylogins.entity.Login;
import ru.inno.tech.mylogins.entity.Model;
import ru.inno.tech.mylogins.entity.User;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class LoginReader implements ModelSupplier {
    @Override
    public Model read(String dirName) {
        List<File> fileLst = getFiles(dirName);
        List<String> rowLst = new ArrayList<>();
        Model model = new Model();
        model.setFileList(fileLst);

        for (File file : fileLst) {
            rowLst = HandFile(file);
            for (String str : rowLst) {
                // разбить предложение на массив слов
                String[] words = str.split("\\|");
                if(words.length==0) continue;
                String userName = words[0].trim();
                if(words.length==1) continue;
                String fio = words[1].trim();
                if(words.length==2) continue;
                String sDt = words[2].trim();
                LocalDateTime dDt;
                if (sDt==null || sDt=="") { dDt = null;}
                else {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
                    dDt = LocalDateTime.parse(sDt, dtf);
                }
                if(words.length==3) continue;
                String sApp = words[3].trim();
                //---
                User user = new User(userName, fio);
                User user1 = model.addUser(user);
                Login login = new Login(null, dDt, sApp, file);
                user1.addLogin(login);
                model.addLogin(login);
            }
        }
        return model;
    }

    // Список файлов в папке типа .txt
    public List<File> getFiles (String dirName){
         List<File> rez = new ArrayList<>();
         File fl = new File(dirName);
         File[] ff = fl.listFiles();
        for (int i = 0; i < ff.length; i++) {
            if (ff[i].isFile() && ff[i].getName().endsWith(".txt"))
                rez.add(ff[i]);
        }
         return rez;
    }
    // Обработка файла
    public List<String> HandFile (File file){
        List<String> lst = new ArrayList<>();
        try(Scanner sc = new Scanner(file)) {
            while(sc.hasNextLine()){
                String s = sc.nextLine().trim();
                lst.add(s);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return lst;
    }

}