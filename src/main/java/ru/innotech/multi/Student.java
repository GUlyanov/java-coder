package ru.innotech.multi;

public class Student implements StudInt {
    private String name;
    private int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Cache
    public String getName(String name) {
        return name == null ? this.name : name;
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

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}