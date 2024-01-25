package ru.inno.tech.products.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="agreement")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class Agreement {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private ProdType type;

    private String number;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer days;
    private String reasonClose;
    private String state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o==null) return false;
        if (!(o instanceof Agreement agr)) return false;
        if (this.id == agr.id) return true;
        if (this.id != agr.id) return false;
        return this.product.equals(agr.product) &&
                type == agr.type &&
                Objects.equals(number, agr.number);
    }
    @Override
    public int hashCode() {
        return Objects.hash(product, type, number);
    }


}
