package ru.innotech.multi;

import org.junit.jupiter.api.*;

public class TestMulti {
    @Test
    public void TestProxy() throws Exception {
        long objLifeTimeOut = 30*1000L; // время жизни ключа - 30 сек
        long cacheClsRate = 1*60*1000L; // частота запуска очистки кэша - 1 мин

        // студент
        Student student = new Student("Петя", 18);
        StudInt st = Utils.cache(student, objLifeTimeOut, cacheClsRate);
        // студент1
        Student student1 = new Student("Петя1", 17);
        StudInt st1 = Utils.cache(student1, objLifeTimeOut, cacheClsRate);

        // студент
        Assertions.assertEquals(st.getName("Гена"), "Гена");     // значение вычислено
        Assertions.assertEquals(st.getName("Вася"), "Гена");     // значение из кэша
        Assertions.assertEquals(st.getName("Игорь"), "Гена");    // значение из кэша
        st.setName("Боря"); st.setAge(21);                              // состояние изменено
        Assertions.assertEquals(st.getName("Игорь"), "Игорь");    // значение вычислено
        Assertions.assertEquals(st.getName("Сергей"), "Игорь");   // значение из кэша
        st.setName("Петя"); st.setAge(20);                               // состояние изменено но отличается от бывшего возрастом
        Assertions.assertEquals(st.getName("Миша"), "Миша");       // значение вычислено
        Assertions.assertEquals(st.getName("Сергей"), "Миша");     // значение из кэша
        st.setName("Петя"); st.setAge(18);                                // состояние изменено на уже бывшее
        Assertions.assertEquals(st.getName("Борис"), "Гена");       // значение из кэша
        Assertions.assertEquals(st.getName("Сергей"), "Гена");      // значение из кэша
        // студент1
        Assertions.assertEquals(st1.getName("Гена1"), "Гена1");     // значение вычислено
        Assertions.assertEquals(st1.getName("Вася1"), "Гена1");     // значение из кэша
        Assertions.assertEquals(st1.getName("Игорь1"), "Гена1");    // значение из кэша
        st1.setName("Боря1"); st1.setAge(20);                              // состояние изменено
        Assertions.assertEquals(st1.getName("Игорь1"), "Игорь1");    // значение вычислено
        Assertions.assertEquals(st1.getName("Сергей1"), "Игорь1");   // значение из кэша
        st1.setName("Петя1"); st1.setAge(19);                               // состояние изменено но отличается от бывшего возрастом
        Assertions.assertEquals(st1.getName("Миша1"), "Миша1");       // значение вычислено
        Assertions.assertEquals(st1.getName("Сергей1"), "Миша1");     // значение из кэша
        st1.setName("Петя1"); st1.setAge(17);                                // состояние изменено на уже бывшее
        Assertions.assertEquals(st1.getName("Борис1"), "Гена1");       // значение из кэша
        Assertions.assertEquals(st1.getName("Сергей1"), "Гена1");      // значение из кэша

        System.out.println("Тесты до очистки завершены");
        Thread.sleep(60000);                                         // засыпаем на минуту, кэш должен очиститься
        System.out.println("Тесты после очистки начаты");
        // студент
        st.setName("Боря");  st.setAge(21);                                // состояние изменено на уже бывшее
        Assertions.assertEquals(st.getName("Миша"), "Миша");         // значение вычислено (в кэше ключа нет)
        Assertions.assertEquals(st.getName("Тимофей"), "Миша");      // значение из кэша
        // студент1
        st1.setName("Боря1");  st1.setAge(20);                                // состояние изменено на уже бывшее
        Assertions.assertEquals(st1.getName("Миша1"), "Миша1");         // значение вычислено (в кэше ключа нет)
        Assertions.assertEquals(st1.getName("Тимофей1"), "Миша1");      // значение из кэша
        System.out.println("Тесты после очистки завершены");
    }

}
