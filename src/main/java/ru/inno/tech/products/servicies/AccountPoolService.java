package ru.inno.tech.products.servicies;

import org.springframework.stereotype.Service;
import ru.inno.tech.products.entities.AccountPool;
import ru.inno.tech.products.exceptions.AccountNotFoundException;
import ru.inno.tech.products.repositories.AccountPoolRepository;

import java.util.Optional;
import java.util.Random;

@Service
public class AccountPoolService implements AccountPoolServiceInt {
    AccountPoolRepository accPoolRep;

    public AccountPoolService(AccountPoolRepository accPoolRep) {
        this.accPoolRep = accPoolRep;
    }

    // Запрос у генератора счетов строки пула счета для заданного типа регистра и привязка его к продуктовому регистру договора
    public AccountPool getAccountPool(AccountPool accPool, boolean doEx){
        var p1 = accPool.getBranchCode();
        var p2 = accPool.getCurrencyCode();
        var p3 = accPool.getMdmCode();
        var p4 = accPool.getPriority();
        var p5 = accPool.getRegistryTypeCode();
        Optional<AccountPool> accPoolOp = accPoolRep.getAccountPool(p1, p2, p3, p4, p5);
        if (accPoolOp.isEmpty()) {
            if (doEx) throw new AccountNotFoundException(null, p1, p2, p3, p4, p5);
            else return null;
        }
        return accPoolOp.get();
    }

    // Запрос у генератора счетов номера счета для заданного типа регистра и привязка его к продуктовому регистру договора
    public String getAccount(AccountPool accPool){
        if (accPool==null) return null;
        String[] words = accPool.getAccounts().split(",");
        if(words.length==0) return null;
        int n = (new Random()).nextInt(words.length);
        return words[n].trim();
    }

}
