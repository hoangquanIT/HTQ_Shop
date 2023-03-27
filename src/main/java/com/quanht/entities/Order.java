package com.quanht.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

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
        this.status = OrderStatus.NEW;
        this.payment = OrderPayment.UNPAID;
        this.fulfillment = OrderFulfillment.UNFULFILLED;
    }

}
