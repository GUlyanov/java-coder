package ru.inno.tech.mylogins.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.inno.tech.mylogins.entity.Login;


public interface LoginRepository extends JpaRepository<Login, Long> {

}
