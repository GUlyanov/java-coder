package ru.inno.tech.mylogins.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.io.File;
import java.time.LocalDateTime;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Table(name = "logins")
public class Login {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "USER_ID")

    private User user;
    private LocalDateTime accessDate;
    private String application;
    @Transient
    private String error;
    @Transient
    private File file;

    public Login(Long id, LocalDateTime accessDate, String application, File file) {
        this.id = id;
        this.user = null;
        this.accessDate = accessDate;
        this.application = application;
        this.error = null;
        this.file = file;
    }

    public Login() {
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(LocalDateTime accessDate) {
        this.accessDate = accessDate;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getFileName() {
        return file.getName();
    }

    @Override
    public String toString() {
        return "Login{" +
                "user=" + user +
                ", accessDate=" + accessDate +
                ", application='" + application + '\'' +
                '}';
    }


}
