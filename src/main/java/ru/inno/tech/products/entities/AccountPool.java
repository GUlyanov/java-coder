package ru.inno.tech.products.entities;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="account_pool")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class AccountPool {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Integer id;
    private String branchCode;
    private String currencyCode;
    private String mdmCode;
    @Column(name="xpriority")
    private String priority;
    private String registryTypeCode;
    private String accounts;
}
