package com.patika.akbankservice.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class LoanLimit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "max_amount")
    private BigDecimal maxAmount;
    
    @Column(name = "min_amount")
    private BigDecimal minAmount;

    @Column(name = "interest_rate")
    private Double interestRate;

    @Column(name = "installment")
    private Integer installment;
}
