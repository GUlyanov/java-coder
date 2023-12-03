package ru.innotech.accs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;

public class TestAccount {
    @Test
    public void TestAcc1()  {
        Account acc = new Account("Иванов П.Р.");
        acc.addCur(Currency.EUR, BigDecimal.valueOf(12.34));
        Assertions.assertEquals(acc.getAmount(Currency.EUR), BigDecimal.valueOf(12.34));
    }

    @Test
    public void TestAcc2()  {
        Account acc = new Account("Иванов П.Р.");
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->acc.addCur(Currency.EUR, BigDecimal.valueOf(-2.00)));
    }

    @Test
    public void TestAcc3()  {
        Account acc = new Account("Иванов П.Р.");
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->acc.setClientName(""));
    }

    @Test
    public void TestUndo1(){
        Account acc = new Account("dd1");
        acc.setClientName("dd2");
        acc.addCur(Currency.EUR, BigDecimal.valueOf(105.20));
        acc.addCur(Currency.EUR, BigDecimal.valueOf(200.50));
        acc.setClientName("dd3");
        acc.undo();
        Assertions.assertEquals(acc.getClientName(), "dd2");
        acc.undo();
        Assertions.assertEquals(acc.getAmount(Currency.EUR), BigDecimal.valueOf(105.20));
        acc.undo();
        Assertions.assertNull(acc.getAmount(Currency.EUR));
        acc.undo();
        Assertions.assertEquals(acc.getClientName(), "dd1");
        Assertions.assertNull(acc.getAmount(Currency.EUR));
        Assertions.assertThrows(RuntimeException.class, acc::undo);
    }

    @Test
    public void TestSaveRestore(){
        // создали счет ac1
        Account ac1 = new Account("Иванов");
        ac1.addCur(Currency.EUR, BigDecimal.valueOf(12.34));
        ac1.addCur(Currency.USD, BigDecimal.valueOf(100.26));
        Account av1 = new Account(ac1);
        // сделали копию № 1 счета ac1 - acp1
        AccCopy acp1 = ac1.save();
        AccCopy acv1 = new AccCopy(acp1);
        // изменили счет ac1
        ac1.setClientName("Иванидзе");
        ac1.addCur(Currency.EUR, BigDecimal.valueOf(36.28));
        ac1.addCur(Currency.RUR, BigDecimal.valueOf(500.20));
        Account av2 = new Account(ac1);
        // проверить, что копия acp1 не изменилась при изменении ac1 (acp1c = acp1)
        Assertions.assertEquals(acp1, acv1);
        // сделали копию № 2 счета ac1 - acp1
        AccCopy acp2 = ac1.save();
        // восстановили на счете ac1 копию № 1 (acp1)
        ac1.restore(acp1);
        // проверить, что состояние счета восстановилось к состоянию на момент взятия копии № 1
        Assertions.assertEquals(ac1, av1);
        // восстановили на счете ac1 копию № 2 (acp2)
        ac1.restore(acp2);
        // проверить, что состояние счета восстановилось к состоянию на момент взятия копии № 2
        Assertions.assertEquals(ac1, av2);
    }

    @Test
    public void TestSaveRestoreSer(){
        // создали счет ac1
        Account ac1 = new Account("Иванов");
        ac1.addCur(Currency.EUR, BigDecimal.valueOf(12.34));
        ac1.addCur(Currency.USD, BigDecimal.valueOf(100.26));
        Account av1 = new Account(ac1);
        // сделали копию № 1 счета ac1 - acp1
        ac1.saveSer();
        // изменили счет ac1
        ac1.setClientName("Иванидзе");
        ac1.addCur(Currency.EUR, BigDecimal.valueOf(36.28));
        ac1.addCur(Currency.RUR, BigDecimal.valueOf(500.20));
        // восстановили на счете ac1 копию № 1 (acp1)
        ac1.restoreSer();
        // проверить, что состояние счета восстановилось к состоянию на момент взятия копии № 1
        Assertions.assertEquals(ac1, av1);
        // удалить файл
        new File(Account.FILE).delete();
    }

}
