package ru.innotech.multi;

import org.junit.jupiter.api.*;

public class TestMulti {

    @Test
    @DisplayName("1.Первый вызов функции: вычисление значения")
    public void Test1() throws Exception {
        long cacheClsRate = 0L;   // очистка кеша выключена

        // дробь
        Fraction fr1 = new Fraction(2,10);
        Fractionable num1 = Utils.cache(fr1, cacheClsRate);
        // первый вызов функции
        Assertions.assertEquals(0.2, num1.doubleValue(), "Значение функции");
        Assertions.assertEquals(ResultType.IN_CACHE, Utils.resTp, "Значение вычислено, записано в кеш");
        Assertions.assertEquals(1, Utils.mapCnt, "В кеше записей");
    }

    @Test
    @DisplayName("2.Повторный вызов функции: значение из кеша")
    public void Test2() throws Exception {
        long cacheClsRate = 0L;   // отключена очистка кеша

        // дробь1
        Fraction fr1 = new Fraction(2,10);
        Fractionable num1 = Utils.cache(fr1, cacheClsRate);
        // первый вызов функции
        num1.doubleValue();
        // повторный вызов функции
        Assertions.assertEquals(0.2, num1.doubleValue(), "Значение функции");
        Assertions.assertEquals(ResultType.FROM_CACHE, Utils.resTp, "Значение из кеша");
        Assertions.assertEquals(1, Utils.mapCnt, "В кеше записей");
    }
    @Test
    @DisplayName("3.Вызов функции после изменения состояния объекта - вычислить значение")
    public void Test3() throws Exception {
        long cacheClsRate = 0L;   // отключена очистка кеша

        // дробь1
        Fraction fr1 = new Fraction(2,10);
        Fractionable num1 = Utils.cache(fr1, cacheClsRate);

        // первый вызов функции
        num1.doubleValue();
        // повторный вызов функции
        num1.doubleValue();
        // изменение состояния объекта
        num1.setNum(3);
        // вызов функции после изменения состояния
        Assertions.assertEquals(0.3, num1.doubleValue(), "Значение функции");
        Assertions.assertEquals(ResultType.IN_CACHE, Utils.resTp, "Значение вычислено, записано в кеш");
        Assertions.assertEquals(2, Utils.mapCnt, "В кеше записей");
    }
    @Test
    @DisplayName("4.Вызов функции после изменения состояния объекта на уже бывшее - значение из кеша")
    public void Test4() throws Exception {
        long cacheClsRate = 0L;  // отключена очистка кеша

        // дробь1
        Fraction fr1 = new Fraction(2,10);
        Fractionable num1 = Utils.cache(fr1, cacheClsRate);

        // первый вызов функции
        num1.doubleValue();
        // повторный вызов функции
        num1.doubleValue();
        // изменение состояния объекта
        num1.setNum(3);
        // вызов функции
        num1.doubleValue();
        // изменение состояния объекта на уже бывшее - значение из кеша
        num1.setNum(2);
        // вызов функции после изменения состояния
        Assertions.assertEquals(0.2, num1.doubleValue(), "Значение функции");
        Assertions.assertEquals(ResultType.FROM_CACHE, Utils.resTp, "Значение из кеша");
        Assertions.assertEquals(2, Utils.mapCnt, "В кеше записей");
    }

    @DisplayName("5.Вызов функции после возврата старого состояния объекта, но значение в кеше устарело - вычислить значение")
    @RepeatedTest(5)
    public void Test5() throws Exception {
        long cacheClsRate = 2000L;   // частота запуска очистки кэша - 2 сек

        // дробь1
        Fraction fr1 = new Fraction(2,10);
        Fractionable num1 = Utils.cache(fr1, cacheClsRate);

        // первый вызов функции
        num1.doubleValue();
        // повторный вызов функции
        num1.doubleValue();
        // изменение состояния объекта
        num1.setNum(3);
        // вызов функции
        num1.doubleValue();
        // пауза на 3 сек, чтобы значения в кеше устарели
        Thread.sleep(3000);
        // подберем мусор, чтоб наверняка
        System.gc();
        // пауза на 20 сек, чтобы сборщик мусора убрал из кеша устаревшие записи. Вычислить новое значение
        Thread.sleep(20000);
        // изменение состояния объекта на уже бывшее - а в кеше то ничего
        num1.setNum(2);
        // вызов функции после изменения состояния
        Assertions.assertEquals(0.2, num1.doubleValue(), "Значение функции");
        Assertions.assertEquals(ResultType.IN_CACHE, Utils.resTp, "Значение вычислено, занесено в кеш");
        Assertions.assertEquals(1, Utils.mapCnt, "В кеше записей");
    }

}
