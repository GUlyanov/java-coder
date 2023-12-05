package ru.innotech.proxy;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        Student student = new Student("Петя", 20);
        StudInt st = Utils.<Student>cache(student);
        System.out.println(st.getName());
        System.out.println(st.getName());
        st.setName("Гриша");
        System.out.println(st.getName());
    }
}
