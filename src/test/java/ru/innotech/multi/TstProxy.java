package ru.innotech.multi;

import org.junit.jupiter.api.*;
import ru.innotech.multi.StudInt;
import ru.innotech.multi.Student;
import ru.innotech.multi.Utils;

public class TstProxy {
    @Test
    public void TestProxy() throws ClassNotFoundException {
        long objLifeTimeOut = 5*60*1000L;
        long cacheClsRate = 1*60*1000L;

        Student student = new Student("Петя", 18);
        StudInt st = Utils.<Student>cache(student, objLifeTimeOut, cacheClsRate);

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
