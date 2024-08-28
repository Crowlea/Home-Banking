/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.homebanking.Banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.homebanking.Banking.entity.Account;

/**
 *
 * @author crowl
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
    
    public Account findByNumber(String number);
    
}
