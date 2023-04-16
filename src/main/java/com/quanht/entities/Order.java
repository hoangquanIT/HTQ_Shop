package com.quanht.entities;

import com.quanht.dto.AccountDto;
import com.quanht.dto.StatisticsDto;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NamedNativeQuery(
        name = "statistics",
        query = "SELECT MONTH(updated_at) AS months, SUM(total) AS total " +
                "FROM orders o " +
                "WHERE payment = 'PAID' AND YEAR(updated_at) = YEAR(CURDATE()) " +
                "GROUP BY MONTH(updated_at)",
        resultSetMapping = "StatisticsDto"
)
@SqlResultSetMapping(
        name = "StatisticsDto",
        classes = @ConstructorResult(
                targetClass = StatisticsDto.class,
                columns = {
                        @ColumnResult(name = "months", type = Integer.class),
                        @ColumnResult(name = "total", type = Double.class)
                }
        )
)

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(generator = "prod-generator")
    @GenericGenerator(name = "prod-generator",

            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = "DH"),

            strategy = "com.quanht.config.CustomGenerateId")
    @Column(name = "id")
    private String id;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "total")
    private Double total;

    @Column(name = "cart_id")
    private Long cartId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "payment")
    @Enumerated(EnumType.STRING)
    private OrderPayment payment;

    @Column(name = "fulfillment")
    @Enumerated(EnumType.STRING)
    private OrderFulfillment fulfillment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne(mappedBy = "order", orphanRemoval = true)
    private ShippingAddress shippingAddress;

    @PrePersist
    public void prePersist(){
        this.setCreatedAt(LocalDateTime.now());
        this.setUpdatedAt(LocalDateTime.now());
        this.status = OrderStatus.NEW;
        this.payment = OrderPayment.UNPAID;
        this.fulfillment = OrderFulfillment.UNFULFILLED;
    }

}
