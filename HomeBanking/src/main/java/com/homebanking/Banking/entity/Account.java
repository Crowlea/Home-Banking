/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homebanking.Banking.entity;

import com.homebanking.Banking.enums.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author crowl
 */
@Entity
@Table(name = "cuentas")
@Data
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "account_type")
    private AccountType type;
    
    @Column(unique= true)
    private String number;
    
    @Column(unique = true)
    private String cbu;
    
    @Column(unique = true)
    private String alias;
    
    private BigDecimal amount;
    
    @ManyToOne
    private User owner;
    
}
