/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homebanking.Banking.entity;

import com.homebanking.Banking.enums.CardColor;
import com.homebanking.Banking.enums.CardType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author crowl
 */
@Entity
@Data
@Table(name = "tarjetas")
public class Card {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String cardHolder;
    
    private String number;
    
    private CardType type;
    
    private CardColor color;
    
    private Integer cvv;
    
    private LocalDateTime fromDate;
    
    private LocalDateTime thruDate;
    
    private boolean isActive;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")/*especifica un nombre para la columna en la base de datos*/
    private User owner;
    
}
