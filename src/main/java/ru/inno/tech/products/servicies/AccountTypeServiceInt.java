package ru.inno.tech.products.servicies;

import ru.inno.tech.products.entities.AccountType;

public interface AccountTypeServiceInt {
    AccountType findAccTypeByValue(String number, boolean doEx);
}
