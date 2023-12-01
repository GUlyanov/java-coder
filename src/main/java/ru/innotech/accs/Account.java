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

    public Account(Account original) {
        this.clientName = original.clientName;
        this.rest = original.getRest();
    }

    public String getClientName() {
        return clientName;
    }

    // Получение списка остатков по валютам (глубокая копия)
    public Map<Currency, BigDecimal> getRest() {
        HashMap<Currency,BigDecimal> rez = new HashMap<Currency,BigDecimal>();
        for (Map.Entry<Currency, BigDecimal> entry : rest.entrySet()) {
            rez.put(entry.getKey(), entry.getValue());
        }
        return rez;
    }


    public void undo(){
        if (hist.isEmpty()) throw new RuntimeException("Нет изменений для отката!");
        Runnable hs = hist.pop();
        hs.run();
    }

    public void setClientName(String clientName) {
        if (clientName==null || clientName.isEmpty()) throw new IllegalArgumentException("Пустое имя клиента!");
        String oldName = this.clientName;
        this.clientName = clientName;
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
        return new AccCopy(this);
    }

    public void restore(AccCopy accCopy){
        this.clientName = accCopy.getClientName();
        this.rest = accCopy.getRest();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return  Objects.equals(getClientName(), account.getClientName()) &&
                Objects.equals(rest, account.rest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClientName(), rest);
    }

    public Object clone(){
        Account account = new Account(getClientName());
        account.rest = new HashMap<Currency,BigDecimal>(rest);
        return (Object) account;
    }

    public Object clone1(){
        Account account = new Account(getClientName());
        account.rest = new HashMap<Currency,BigDecimal>();
        for (Map.Entry<Currency, BigDecimal> entry : rest.entrySet()) {
            account.rest.put(entry.getKey(), entry.getValue());
        }
        return (Object) account;
    }

    static public HashMap<Currency,BigDecimal> copyHash(HashMap<Currency,BigDecimal> original){
        HashMap<Currency,BigDecimal> rez = new HashMap<Currency,BigDecimal>();
        for (Map.Entry<Currency, BigDecimal> entry : original.entrySet()) {
            rez.put(entry.getKey(), entry.getValue());
        }
        return rez;
    }



}

