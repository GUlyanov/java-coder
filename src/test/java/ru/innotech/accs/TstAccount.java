package ru.innotech.accs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;

public class TstAccount {
    @Test
    public void TstAcc1() throws Exception {
        Account acc = new Account("Иванов П.Р.");
        acc.setCurr(Currency.EUR, BigDecimal.valueOf(12.34));
        Assertions.assertEquals(acc.getAmount(Currency.EUR), BigDecimal.valueOf(12.34));
    }

    @Test
    public void TstAcc2()  {
        Account acc = new Account("Иванов П.Р.");
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->acc.setCurr(Currency.EUR, BigDecimal.valueOf(-2.00)));
    }

    @Test
    public void TstAcc3()  {
        Account acc = new Account("Иванов П.Р.");
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->acc.setClientName(""));
    }

    @Test
    public void TstUndo1(){
        Account acc = new Account("dd1");
        acc.setClientName("dd2");
        acc.setCurr(Currency.EUR, BigDecimal.valueOf(105.20));
        acc.setCurr(Currency.EUR, BigDecimal.valueOf(200.50));
        acc.setClientName("dd3");
        acc.undo();
        Assertions.assertEquals(acc.getClientName(), "dd2");
        acc.undo();
        Assertions.assertEquals(acc.getAmount(Currency.EUR), BigDecimal.valueOf(105.20));
        acc.undo();
        Assertions.assertEquals(acc.getAmount(Currency.EUR), null);
        acc.undo();
        Assertions.assertEquals(acc.getClientName(), "dd1");
        Assertions.assertEquals(acc.getAmount(Currency.EUR), null);
        Assertions.assertThrows(RuntimeException.class, ()->acc.undo());
    }

    @Test
    public void TstSaveRestore(){
        // создали счет ac1
        Account ac1 = new Account("Иванов");
        ac1.setCurr(Currency.EUR, BigDecimal.valueOf(12.34));
        ac1.setCurr(Currency.USD, BigDecimal.valueOf(100.26));
        Account av1 = new Account(ac1);
        // сделали копию № 1 счета ac1 - acp1
        AccCopy acp1 = ac1.save();
        AccCopy acv1 = new AccCopy(acp1);
        // изменили счет ac1
        ac1.setClientName("Иванидзе");
        ac1.setCurr(Currency.EUR, BigDecimal.valueOf(36.28));
        ac1.setCurr(Currency.RUR, BigDecimal.valueOf(500.20));
        Account av2 = new Account(ac1);
        // проверить, что копия acp1 не изменилась при изменении ac1 (acp1c = acp1)
        Assertions.assertEquals(acp1.equals(acv1), true);
        // сделали копию № 2 счета ac1 - acp1
        AccCopy acp2 = ac1.save();
        // восстановили на счете ac1 копию № 1 (acp1)
        ac1.restore(acp1);
        // проверить, что состояние счета восстановилось к состоянию на момент взятия копии № 1
        Assertions.assertEquals(ac1.equals(av1), true);
        // восстановили на счете ac1 копию № 2 (acp2)
        ac1.restore(acp2);
        // проверить, что состояние счета восстановилось к состоянию на момент взятия копии № 2
        Assertions.assertEquals(ac1.equals(av2), true);
    }
}
