/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.homebanking.Banking.services;

import com.homebanking.Banking.entity.Loan;
import java.util.List;

/**
 *
 * @author crowl
 */
public interface ILoanService {
    
    public List<Loan> getAllLoan();

    public Loan getLoanById(Long id);

    public void saveLoan(Loan loan);
    
}
