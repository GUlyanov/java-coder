package ru.inno.tech.products.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.inno.tech.products.entities.Agreement;
import ru.inno.tech.products.entities.Product;
import java.util.Optional;

public interface AgreementRepository extends JpaRepository<Agreement, Integer> {

    Optional<Agreement> findAgreementByProductAndNumber(Product product, String number);
}



