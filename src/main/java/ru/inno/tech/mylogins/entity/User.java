package ru.inno.tech.mylogins.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(name = "username")
    private String userName;
    private String fio;

    @OneToMany(mappedBy = "user", orphanRemoval=true)
    private final Set<Login> logins = new HashSet<>();

    public User(String userName, String fio) {
        this.userName = userName;
        this.fio = fio;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public Set<Login> getLogins() {
        return logins;
    }

    public boolean addLogin(Login login) {
        login.setUser(this);
        return getLogins().add(login);
    }

    public void removeLogin(Login login) {
        getLogins().remove(login);
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", fio='" + fio + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getUserName(), user.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName());
    }
}
