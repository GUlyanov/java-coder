package ru.inno.tech.products.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.inno.tech.products.entities.*;

import java.util.Optional;
import java.util.Set;

public interface ProductRegisterTypeRepository extends JpaRepository<ProductRegisterType, Integer> {
    Optional<ProductRegisterType> findProductRegisterTypeByValue(String value);
    Set<ProductRegisterType> findProductRegisterTypeByProductClassAndAccountType(ProductClass productClass, AccountType accountType);

}
