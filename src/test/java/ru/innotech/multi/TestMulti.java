package ru.innotech.multi;

import org.junit.jupiter.api.*;

public class TestMulti {
    @Test
    public void TestProxy() throws Exception {
        long cacheClsRate = 2*1000L;   // частота запуска очистки кэша - 2 сек

        // дробь1
        Fraction fr1 = new Fraction(2,3);
        Fractionable num1 = Utils.cache(fr1, cacheClsRate);

        System.out.println("Тесты до очистки начаты");
        System.out.println("Тест1");
        num1.doubleValue(); // sout сработал
        num1.doubleValue(); // sout молчит

        System.out.println("Тест2");
        num1.setNum(1);
        num1.doubleValue(); // sout сработал
        num1.doubleValue(); // sout молчит

        System.out.println("Тест3");
        num1.setNum(2); num1.setDenum(7);
        num1.doubleValue(); // sout сработал
        num1.doubleValue(); // sout молчит

        System.out.println("Тест4");
        num1.setNum(2); num1.setDenum(3);
        num1.doubleValue(); // sout молчит
        num1.doubleValue(); // sout молчит

        System.out.println("Тесты до очистки завершены");
        Thread.sleep(2000);                                         // засыпаем на 2 сек, кэш должен очиститься
        System.out.println("Тесты после очистки начаты");

        System.out.println("Тест5");
        num1.doubleValue(); // sout сработал
        num1.doubleValue(); // sout молчит
        System.out.println("Тесты после очистки завершены");
    }

}
