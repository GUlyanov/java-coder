package ru.inno.tech.mylogins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("ru.inno.tech.mylogins.repositories")
@EnableAspectJAutoProxy
@SpringBootApplication
public class MyloginsApplication {
	public static void main(String[] args) {
		SpringApplication.run(MyloginsApplication.class, args);
	}
}
