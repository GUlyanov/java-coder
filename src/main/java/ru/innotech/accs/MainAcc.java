package ru.innotech.accs;

import java.math.BigDecimal;

public class MainAcc {
    public static void main(String[] args) {
        Account acc = new Account("dd1");
        acc.setClientName("dd2");
        acc.setCurr(Currency.EUR, BigDecimal.valueOf(105.20));
        acc.setCurr(Currency.EUR, BigDecimal.valueOf(200.50));
        acc.setClientName("dd3");
        System.out.println(acc.toString());
        acc.undo();
        System.out.println(acc.toString());
        acc.undo();
        System.out.println(acc.toString());
        acc.undo();
        System.out.println(acc.toString());
        acc.undo();
        System.out.println(acc.toString());
        acc.undo();
        System.out.println(acc.toString());
    }

}
