package ru.inno.tech.products.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.inno.tech.products.entities.Product;
import ru.inno.tech.products.entities.ProductClass;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    Optional<Product> findProductByNumber(String number);
    Optional<Product> findProductById(Integer Id);
}
