package ru.inno.tech.products.servicies;

import ru.inno.tech.products.entities.ProductClass;

public interface ProductClassServiceInt {
    ProductClass findProductClassByValue(String value, boolean doEx);
}
