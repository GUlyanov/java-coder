package ru.inno.tech.products.servicies;

import org.springframework.stereotype.Service;
import ru.inno.tech.products.entities.AccountType;
import ru.inno.tech.products.exceptions.AccTypeByValueNoException;
import ru.inno.tech.products.repositories.AccountTypeRepository;

import java.util.Optional;

@Service
public class AccountTypeService implements AccountTypeServiceInt {
    AccountTypeRepository accTypeRep;

    public AccountTypeService(AccountTypeRepository accTypeRep) {
        this.accTypeRep = accTypeRep;
    }

    // Найти Тип счета в базе с заданным value
    public AccountType findAccTypeByValue(String number, boolean doEx){
        Optional<AccountType> accTp = accTypeRep.findAccountTypeByValue(number);
        if (accTp.isEmpty()) {
            if (doEx) throw new AccTypeByValueNoException(null, number);
            else return null;
        }
        return accTp.get();
    }
}
