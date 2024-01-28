package ru.inno.tech.products.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="tpp_ref_account_type")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class AccountType {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "internal_id")
    private Integer id;

    @Column(name="xvalue")
    private String value;

    public AccountType(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    @OneToMany(mappedBy = "accountType")
    private Set<ProductRegisterType> registerTypes = new HashSet<>();

    public AccountType(String value) {
        this.value = value;
    }

    public void addProductRegisterType(ProductRegisterType registerType){
        registerTypes.add(registerType);
        registerType.setAccountType(this);
    }
}
