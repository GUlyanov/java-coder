package ru.inno.tech.products.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class AccountNotFoundException extends RuntimeException {
    private String branchCode;
    private String currencyCode;
    private String mdmCode;
    private String priority;
    private String registryTypeCode;

    public AccountNotFoundException(String message, String branchCode, String currencyCode, String mdmCode, String priority, String registryTypeCode) {
        super(message);
        this.branchCode = branchCode;
        this.currencyCode = currencyCode;
        this.mdmCode = mdmCode;
        this.priority = priority;
        this.registryTypeCode = registryTypeCode;
    }
}
