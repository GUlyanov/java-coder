package ru.innotech.proxy;

import org.junit.jupiter.api.*;

public class TstProxy {
    @Test
    public void TestProxy() throws ClassNotFoundException {
        Student student = new Student("Петя");
        StudInt st = Utils.<Student>cache(student);

        Assertions.assertEquals(st.getName(null), "Петя");       // значение вычислено
        Assertions.assertEquals(st.getName("Вася"), "Петя");     // значение из кэша
        Assertions.assertEquals(st.getName("Игорь"), "Петя");    // значение из кэша
        st.setName("Боря");                                             // применен Setter
        Assertions.assertEquals(st.getName("Игорь"), "Игорь");    // значение вычислено
        Assertions.assertEquals(st.getName("Сергей"), "Игорь");   // значение из кэша
        st.setName("Михаил");                                            // применен Setter
        Assertions.assertEquals(st.getName(null), "Михаил");       // значение вычислено
        Assertions.assertEquals(st.getName("Сергей"), "Михаил");   // значение из кэша
    }

}
