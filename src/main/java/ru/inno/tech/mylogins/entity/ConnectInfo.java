package ru.inno.tech.mylogins.entity;

public class ConnectInfo {
    private String connStr;
    private String userName;
    private String password;

    public ConnectInfo(String connStr, String userName, String password) {
        this.connStr = connStr;
        this.userName = userName;
        this.password = password;
    }

    public String getConnStr() {
        return connStr;
    }

    public void setConnStr(String connStr) {
        this.connStr = connStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ConnectInfo{" +
                "connStr='" + connStr + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
