/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.homebanking.Banking.services;

import com.homebanking.Banking.entity.Card;
import java.util.List;

/**
 *
 * @author crowl
 */
public interface ICardService {
    
    public List<Card> getAllCards();

    public Card getCardById(Long id);

    public Card getCardByNumber(String number);

    public void saveCard(Card card);
    
}
