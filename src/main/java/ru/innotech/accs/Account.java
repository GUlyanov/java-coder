package ru.innotech.accs;

import java.math.BigDecimal;
import java.util.*;

public class Account {
    private String clientName;
    private Map<Currency, BigDecimal> rest = new HashMap<>();

    private Stack<Runnable> hist = new Stack<>();

    public Account(String clientname) {
        this.clientName = clientname;
    }

    public String getClientName() {
        return clientName;
    }

    public void undo(){
        if (hist.isEmpty()) throw new RuntimeException("Нет изменений для отката!");
        Runnable hs = hist.pop();
        hs.run();
    }

    public void setClientName(String clientName) {
        if (this.clientName ==null || this.clientName =="") throw new IllegalArgumentException("Пустое имя клиента!");
        String oldName = this.clientName;
        this.clientName = this.clientName;
        // история команд
        Runnable ch = ()->this.clientName = oldName;
        hist.push(ch);
    }

    public void setCurr(Currency curr, BigDecimal amount){
        Runnable ch;
        if (amount.doubleValue() < 0) throw new IllegalArgumentException("Количество валюты отрицательное!");
        BigDecimal oldAmount = rest.put(curr, amount);
        // история команд
        if (oldAmount==null) {
            ch = ()->rest.remove(curr);
        } else {
            ch = ()->rest.put(curr, oldAmount);
        }
        hist.push(ch);
    }
    public void delCurr(Currency curr){
        Runnable ch;
        if (curr==null) throw new IllegalArgumentException("Пустая валюта!");
        BigDecimal oldAmount = rest.remove(curr);
        if (oldAmount!=null) {
            ch = () -> rest.put(curr, oldAmount);
            hist.push(ch);
        }
    }

    public BigDecimal getAmount(Currency curr){
        return rest.get(curr);
    }

    @Override
    public String toString() {
        return "Account{" +
                "clientnName='" + clientName + '\'' +
                ", rest=" + rest +
                '}';
    }

    public AccCopy save(){
        return new AccCopy(clientName, rest);
    }

    public void restore(AccCopy accCopy){
        this.clientName = accCopy.getClientName();
        this.rest = accCopy.getRest();
    }


}

