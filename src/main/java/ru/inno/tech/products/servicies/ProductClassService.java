package ru.inno.tech.products.servicies;

import org.springframework.stereotype.Service;
import ru.inno.tech.products.entities.ProductClass;
import ru.inno.tech.products.exceptions.ProductClassByValueNoException;
import ru.inno.tech.products.repositories.ProductClassRepository;

import java.util.Optional;

@Service
public class ProductClassService implements ProductClassServiceInt{
    ProductClassRepository prodClRep;
    public ProductClassService(ProductClassRepository prodClRep) {
        this.prodClRep = prodClRep;
    }

    // Найти класс продукта по коду класса продукта
    public ProductClass findProductClassByValue(String value, boolean doEx){
        Optional<ProductClass> prCl = prodClRep.findProductClassByValue(value);
        if (prCl.isEmpty()) {
            if (doEx) throw new ProductClassByValueNoException(null, value);
            else return null;
        }
        return prCl.get();
    }

}
