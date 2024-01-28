package ru.inno.tech.products.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="tpp_ref_product_register_type")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class ProductRegisterType {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "internal_id")
    private Integer id;

    @Column(name="xvalue")
    private String value;
    private String registerTypeName;

    @ManyToOne
    @JoinColumn(name = "product_class_code", referencedColumnName = "xvalue")
    private ProductClass productClass;

    @ManyToOne
    @JoinColumn(name = "account_type", referencedColumnName = "xvalue")
    private AccountType accountType;

    @OneToMany(mappedBy = "registerType")
    private Set<ProductRegister> registers = new HashSet<>();

    public ProductRegisterType(String value, String registerTypeName, ProductClass productClass, AccountType accountType) {
        this.value = value;
        this.registerTypeName = registerTypeName;
        this.productClass = productClass;
        this.accountType = accountType;
    }

    public ProductRegisterType(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public void addProductRegister(ProductRegister register){
        registers.add(register);
        register.setRegisterType(this);
    }
}
