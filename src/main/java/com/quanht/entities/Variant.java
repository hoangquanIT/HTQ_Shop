package com.quanht.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quanht.dto.AccountDto;
import com.quanht.dto.VariantDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;


@NamedNativeQuery(
        name = "getVariants",
        query = "SELECT v.id, v.sku, v.size, v.color, v.price, v.quantity, " +
                "p.name, v.created_at, v.updated_at " +
                "FROM variant v INNER JOIN product p ON v.product_id = p.id ",
        resultSetMapping = "Variants"
)
@SqlResultSetMapping(
        name = "Variants",
        classes = @ConstructorResult(
                targetClass = VariantDto.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "sku", type = String.class),
                        @ColumnResult(name = "size", type = String.class),
                        @ColumnResult(name = "color", type = String.class),
                        @ColumnResult(name = "price", type = Double.class),
                        @ColumnResult(name = "quantity", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "created_at", type = LocalDateTime.class),
                        @ColumnResult(name = "updated_at", type = LocalDateTime.class)
                }
        )
)

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "variant")
public class Variant extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Column(name = "size", nullable = false)
    private String size;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "price", nullable = false)
    @Min(value = 0)
    private Double price;

    @Column(name = "quantity", nullable = false)
    @Min(value = 0)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @PrePersist
    void prePersist(){
        this.setCreatedAt(LocalDateTime.now());
        this.setUpdatedAt(LocalDateTime.now());
        this.quantity = 0;
    }

}
