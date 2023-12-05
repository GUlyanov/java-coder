package ru.innotech.proxy;

import org.junit.jupiter.api.*;

public class TstProxy {
    @Test
    public void TestProxy() throws ClassNotFoundException {
        Student student = new Student("Петя", 20);
        StudInt st1 = Utils.<Student>cache(student);
        student = new Student("Макар", 50);
        StudInt st2 = Utils.<Student>cache(student);

        System.out.println("#1.1");
        Assertions.assertEquals(st1.getName(),"Петя");
        System.out.println("#1.2");
        Assertions.assertEquals(st1.getName(),"Петя");
        System.out.println("#2.1");
        Assertions.assertEquals(st2.getName(),"Макар");
        System.out.println("#2.2");
        Assertions.assertEquals(st2.getName(),"Макар");
        System.out.println("#1.3");
        st1.setName("Коля");
        System.out.println("#1.4");
        Assertions.assertEquals(st1.getName(),"Коля");
        System.out.println("#1.5");
        Assertions.assertEquals(st1.getName(),"Коля");
        System.out.println("#2.3");
        st2.setName("Борис");
        System.out.println("#2.4");
        Assertions.assertEquals(st2.getName(),"Борис");
        System.out.println("#2.5");
        Assertions.assertEquals(st2.getName(),"Борис");
        System.out.println("#1.6");
        Assertions.assertEquals(st1.getAge(),20);
        System.out.println("#1.7");
        Assertions.assertEquals(st1.getAge(),20);
        System.out.println("#2.6");
        Assertions.assertEquals(st2.getAge(),50);
        System.out.println("#2.7");
        Assertions.assertEquals(st2.getAge(),50);
        System.out.println("#2.8");
        st2.setAge(60);
        System.out.println("#2.9");
        Assertions.assertEquals(st2.getAge(),60);
        System.out.println("#2.10");
        Assertions.assertEquals(st2.getAge(),60);
    }

}
