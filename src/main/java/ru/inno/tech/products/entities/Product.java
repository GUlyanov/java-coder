package ru.inno.tech.products.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Table(name="tpp_product")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class Product {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_code_id", referencedColumnName = "internal_id")
    private ProductClass productClass;

    private String clientId;

    @Enumerated(EnumType.STRING)
    private ProdType type;

    private String number;

    private String priority;
    private LocalDate dateOfConclusion;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer days;
    private BigDecimal penaltyRate;
    private BigDecimal nso;
    private BigDecimal thresholdAmount;
    private String requisiteType;
    private String interestRateType;
    private BigDecimal taxRate;
    private String reasonClose;
    private String state;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private final Set<Agreement> agreements = new HashSet<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private final Set<ProductRegister>  registers = new HashSet<>();

    @Transient
    private Map<String, String> additionalPropertiesVip;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o==null) return false;
        if (!(o instanceof Product product)) return false;
        if (this.id == product.id) return true;
        if (this.id != product.id) return false;
        return type == product.type && Objects.equals(number, product.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, number);
    }

    public void addAgreement(Agreement agreement){
        agreements.add(agreement);
        agreement.setProduct(this);
    }

    public void addProductRegister(ProductRegister register){
       registers.add(register);
        register.setProduct(this);
    }

}
