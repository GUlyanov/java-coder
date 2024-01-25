package ru.inno.tech.products.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.inno.tech.products.entities.Agreement;
import ru.inno.tech.products.entities.Product;
import ru.inno.tech.products.entities.ProductRegister;

import java.util.Optional;
import java.util.Set;

public interface AgreementRepository extends JpaRepository<Agreement, Integer> {

    public Optional<Agreement> findAgreementByProductAndNumber(Product product, String number);
}



