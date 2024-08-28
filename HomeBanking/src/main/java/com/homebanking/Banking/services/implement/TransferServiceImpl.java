/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homebanking.Banking.services.implement;

import com.homebanking.Banking.entity.Transfer;
import com.homebanking.Banking.repositories.AccountRepository;
import com.homebanking.Banking.repositories.TransferRepository;
import com.homebanking.Banking.entity.Account;
import com.homebanking.Banking.services.ITransferService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author crowl
 */
@Service
public class TransferServiceImpl implements ITransferService {

    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Transfer> allTransfer() {
        return transferRepository.findAll();
    }

    @Override
    public Optional<Transfer> findById(Long id) {
        return transferRepository.findById(id);
    }

    @Override
    public Transfer performTransfer(Long originAccountId, Long destinationAccountId, BigDecimal amount) {
        Account originAccount = accountRepository.findById(originAccountId).get();
        Account destinationAccount = accountRepository.findById(destinationAccountId).get();

        if (destinationAccount == null || originAccount == null) {
            return null;
        }

        if (originAccount.getAmount().compareTo(amount) < 0) {
            System.out.println("Advertencia: Fondos insuficientes en la cuenta con id: " + originAccountId);
        }
        //Resto el monto a la cuenta original
        originAccount.setAmount(originAccount.getAmount().subtract(amount));
        //Sumo el monto a la cuenta de ddestino
        destinationAccount.setAmount(destinationAccount.getAmount().add(amount));
        //Guardo nuevamente las cuantas con sus nuevos montos
        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);

        Transfer transfer = new Transfer();
        Date date = new Date();

        transfer.setDate(date);
        transfer.setOrigin(originAccount.getId());
        transfer.setTarget(destinationAccount.getId());
        transfer.setAmount(amount);
        transfer = transferRepository.save(transfer);

        return transfer;
    }

     @Override
    public void saveTransaction(Transfer transfer) {
        transferRepository.save(transfer);
    }

}
