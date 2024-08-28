/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.homebanking.Banking.services;

import com.homebanking.Banking.entity.Account;
import com.homebanking.Banking.entity.User;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author crowl
 */
public interface IAccountService {
    
   public List<Account>allAccount();
    public Optional<Account>findById(Long id);
    public Account save(Account account);
    public Account updateAccount(Account account, Long id);
    public void activateDeactivate(Long id);
    public Account getAccountByNumber(String number);
    
}
