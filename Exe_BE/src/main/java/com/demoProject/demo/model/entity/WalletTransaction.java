package com.demoProject.demo.model.entity;

import com.demoProject.demo.common.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_wallet_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransaction {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", referencedColumnName = "id", nullable = false)
    private Wallet wallet;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private TransactionType type; // DEPOSIT, WITHDRAW, PAYMENT, REFUND

    @Column(name = "description")
    private String description;

    @Column(name = "transaction_time", nullable = false)
    private LocalDateTime transactionTime;

    @PrePersist
    public void onCreate() {
        this.transactionTime = LocalDateTime.now();
    }
}
