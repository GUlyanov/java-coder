package ru.inno.tech.mylogins.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.inno.tech.mylogins.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u WHERE u.userName = :userName")
    List<User> findUserByUserName(@Param("userName") String uName);

}


