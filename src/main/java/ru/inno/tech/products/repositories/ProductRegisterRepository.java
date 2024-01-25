package ru.inno.tech.products.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.inno.tech.products.entities.Product;
import ru.inno.tech.products.entities.ProductRegister;
import ru.inno.tech.products.entities.ProductRegisterType;

import java.util.Optional;
import java.util.Set;

public interface ProductRegisterRepository extends JpaRepository<ProductRegister,Integer> {
    Optional<ProductRegister> findProductRegisterByProductAndRegisterType(Product product, ProductRegisterType regType);
    Set<ProductRegister> findProductRegisterByProduct(Product product);
    void deleteProductRegisterByProductAndRegisterType(Product prod, ProductRegisterType regType);
}
