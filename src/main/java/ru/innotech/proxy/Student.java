package ru.innotech.proxy;

public class Student implements StudInt {
    String name;

    public Student(String name) {
        this.name = name;
    }

    @Cache
    public String getName(String name) {
        return name==null ? this.name : name;
    }

    @Setter
    public void setName(String name) {
        this.name = name;
    }

}
