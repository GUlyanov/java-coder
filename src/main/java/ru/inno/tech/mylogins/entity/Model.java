package ru.inno.tech.mylogins.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private List<Login> loginList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private List<File> fileList = new ArrayList<>();

    public Model() {
    }

    public Model(List<Login> loginList, List<User> userList, List<File> fileList) {
        this.loginList = loginList;
        this.userList = userList;
        this.fileList = fileList;
    }

    public void addLogin(Login login){
        loginList.add(login);
    }

    public User addUser(User user){
        User rez = user;
        int i = userList.indexOf(user);
        if (i==-1)
            userList.add(user);
        else
            rez = userList.get(i);
        return rez;
    }

    public File addFile(File file){
        File rez = file;
        int i = fileList.indexOf(file);
        if (i==-1)
            fileList.add(file);
        else
            rez = fileList.get(i);
        return rez;
    }

    public List<Login> getLoginList() {
        return loginList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    @Override
    public String toString() {
        return "Model{" +
                "loginList=" + loginList +
                ", userList=" + userList +
                ", fileList=" + fileList +
                '}';
    }
}
