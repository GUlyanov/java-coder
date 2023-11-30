package ru.innotech.accs;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AccCopy {
    private String clientName;
    private Map<Currency, BigDecimal> rest;

    public AccCopy(String clientName, Map<Currency, BigDecimal> rest) {
        this.clientName = clientName;
        this.rest = rest;
    }

    public String getClientName() {
        return clientName;
    }

    public Map<Currency, BigDecimal> getRest() {
        return rest;
    }

}
