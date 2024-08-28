/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.homebanking.Banking.services;

import com.homebanking.Banking.entity.Transfer;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author crowl
 */
public interface ITransferService {
    
    public List<Transfer>allTransfer();
    public Optional<Transfer>findById(Long id);
    public Transfer performTransfer(Long originAccountId, Long destinationAccountId, BigDecimal amount);
    public void saveTransaction(Transfer transfer);
    
}
