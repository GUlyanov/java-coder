package ru.innotech.accs;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AccCopy {
    private final String clientName;
    private final Map<Currency, BigDecimal> rest;

    public AccCopy(Account account) {
        this.clientName = account.getClientName();
        this.rest = account.getRest();
    }

    public AccCopy(AccCopy accCopy) {
        this.clientName = accCopy.getClientName();
        this.rest = accCopy.getRest();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccCopy accCopy)) return false;
        return  Objects.equals(getClientName(), accCopy.getClientName()) &&
                Objects.equals(getRest(), accCopy.getRest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClientName(), getRest());
    }

}
