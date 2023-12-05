package ru.innotech.proxy;

public class Student implements StudInt {
    String name;
    int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Cache
    public String getName() {
        return name;
    }

    @Setter
    public void setName(String name) {
        this.name = name;
    }

    @Cache
    public int getAge() {
        return age;
    }

    @Setter
    public void setAge(int age) {
        this.age = age;
    }


}
