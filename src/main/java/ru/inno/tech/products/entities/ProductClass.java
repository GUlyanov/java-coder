package ru.inno.tech.products.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="tpp_ref_product_class")
@Getter @Setter @NoArgsConstructor @ToString
public class ProductClass {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "internal_id")
    private Integer id;

    private String value;
    private String gblCode;
    private String gblName;
    private String productRowCode;
    private String productRowName;
    private String subclassCode;
    private String subclassName;

    @OneToMany(mappedBy = "productClass")
    private final Set<ProductRegisterType> registerTypes = new HashSet<>();

    @OneToMany(mappedBy = "productClass")
    private final Set<Product> products = new HashSet<>();

    public ProductClass(String value, String gblCode, String gblName, String productRowCode, String productRowName, String subclassCode, String subclassName) {
        this.value = value;
        this.gblCode = gblCode;
        this.gblName = gblName;
        this.productRowCode = productRowCode;
        this.productRowName = productRowName;
        this.subclassCode = subclassCode;
        this.subclassName = subclassName;
    }

    public void addProductRegisterType(ProductRegisterType registerType){
        registerTypes.add(registerType);
        registerType.setProductClass(this);
    }

    public void addProduct(Product product){
        products.add(product);
        product.setProductClass(this);
    }

}
