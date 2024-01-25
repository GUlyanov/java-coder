package ru.inno.tech.products;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ProductsApplication {
	public static void main(String[] args) {SpringApplication.run(ProductsApplication.class, args);
	}
}
