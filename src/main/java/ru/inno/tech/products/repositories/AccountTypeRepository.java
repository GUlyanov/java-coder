package ru.inno.tech.products.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.inno.tech.products.entities.AccountType;
import ru.inno.tech.products.entities.ProductClass;

import java.util.List;
import java.util.Optional;

public interface AccountTypeRepository extends JpaRepository<AccountType,Integer> {
    Optional<AccountType> findAccountTypeByValue(String value);
}
