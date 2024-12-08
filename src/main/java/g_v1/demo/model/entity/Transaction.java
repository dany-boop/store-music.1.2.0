package g_v1.demo.model.entity;

import java.util.List;

import g_v1.demo.model.enums.OrderStatus;
import g_v1.demo.model.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "m_transaction")

public class Transaction extends Entities {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "blockchain_tx_hash", unique = true)
    private String blockchainTransactionHash;

    @Column(name = "blockchain_status", length = 50)
    private String blockchainStatus;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TransactionDetail> transactionDetails;

    @Transient
    private Double totalAmount;

    @PostLoad
    @PostPersist
    public void calculateTotalAmount() {
        if (transactionDetails != null) {
            this.totalAmount = transactionDetails.stream()
                    .mapToDouble(TransactionDetail::getTotalPrice)
                    .sum();
        } else {
            this.totalAmount = 0.0;
        }
    }

}
