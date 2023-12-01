package ru.innotech.accs;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class Account implements Serializable {
    private String clientName;
    private Map<Currency, BigDecimal> rest = new HashMap<>();

    private transient Stack<Runnable> hist = new Stack<>();

    public transient static final String FILE = "data.ser";

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
        rez.putAll(rest);
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

    public void saveSer(){
        try {
            FileOutputStream fos = new FileOutputStream(FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
        } catch (Exception e){
            System.out.println("Exception thrown during test: " + e.toString());
        }
    }

    public void restoreSer(){
        try {
            FileInputStream fis = new FileInputStream(FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Account account = (Account) ois.readObject();
            this.clientName = account.getClientName();
            this.rest = account.getRest();
            ois.close();
        } catch (Exception e){
            System.out.println("Exception thrown during test: " + e.toString());
        }
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


    static public HashMap<Currency,BigDecimal> copyHash(HashMap<Currency,BigDecimal> original){
        HashMap<Currency,BigDecimal> rez = new HashMap<Currency,BigDecimal>();
        for (Map.Entry<Currency, BigDecimal> entry : original.entrySet()) {
            rez.put(entry.getKey(), entry.getValue());
        }
        return rez;
    }



}

