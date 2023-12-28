package ru.inno.tech.mylogins.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.inno.tech.mylogins.entity.Login;
import ru.inno.tech.mylogins.entity.User;

import java.util.List;

public interface LoginRepository extends JpaRepository<Login, Long> {

}
