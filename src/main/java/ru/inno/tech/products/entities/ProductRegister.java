package ru.inno.tech.products.entities;

import jakarta.persistence.*;
import lombok.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="tpp_product_register")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ProductRegister {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "xtype", referencedColumnName = "xvalue")
    private ProductRegisterType registerType;

    @Column(name = "account_id")
    private Integer account;
    private String currentCode;
    private String state;
    private String accountNumber;
}
