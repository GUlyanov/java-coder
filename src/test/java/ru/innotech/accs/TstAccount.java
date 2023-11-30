package ru.innotech.accs;

import org.junit.Assert;
import org.junit.Test;
import java.math.BigDecimal;
import java.util.HashMap;

public class TstAccount {
    @Test
    public void TstAcc1() throws Exception {
        Account acc = new Account("Иванов П.Р.");
        acc.setCurr(Currency.EUR, BigDecimal.valueOf(12.34));
        Assert.assertEquals(acc.getAmount(Currency.EUR), BigDecimal.valueOf(12.34));
    }

    @Test
    public void TstAcc2()  {
        Account acc = new Account("Иванов П.Р.");
        Assert.assertThrows(IllegalArgumentException.class,
                ()->acc.setCurr(Currency.EUR, BigDecimal.valueOf(-2.00)));
    }

    @Test
    public void TstAcc3()  {
        Account acc = new Account("Иванов П.Р.");
        Assert.assertThrows(IllegalArgumentException.class,
                ()->acc.setClientName(""));
    }

    @Test
    public void TstUndo1(){
        Account acc = new Account("dd1");
        acc.setClientName("dd2");
        acc.setCurr(Currency.EUR, BigDecimal.valueOf(105.20));
        acc.setCurr(Currency.EUR, BigDecimal.valueOf(200.50));
        acc.setClientName("dd3");
        System.out.println(acc.toString());
        acc.undo();
        Assert.assertEquals(acc.getClientName(), "dd2");
        acc.undo();
        Assert.assertEquals(acc.getAmount(Currency.EUR), BigDecimal.valueOf(105.20));
        acc.undo();
        Assert.assertEquals(acc.getAmount(Currency.EUR), null);
        acc.undo();
        Assert.assertEquals(acc.getClientName(), "dd1");
        Assert.assertEquals(acc.getAmount(Currency.EUR), null);
        Assert.assertThrows(RuntimeException.class, ()->acc.undo());
    }

    /*
    @Test
    public void TstSave{
        Account ac1 = new Account("Иванов");
        ac1.setCurr(Currency.EUR, BigDecimal.valueOf(12.34));
        ac1.setCurr(Currency.USD, BigDecimal.valueOf(100.26));
        AccCopy acp1 = ac1.save();
        ac1.setCurr(Currency.EUR, BigDecimal.valueOf(36.28));
        ac1.setCurr(Currency.RUR, BigDecimal.valueOf(500.20));
        AccCopy acp2 = ac1.save();
        ac1.restore(acp1);
    }
  */
}
