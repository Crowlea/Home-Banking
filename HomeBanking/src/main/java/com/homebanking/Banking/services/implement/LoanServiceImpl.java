/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homebanking.Banking.services.implement;

import com.homebanking.Banking.entity.Loan;
import com.homebanking.Banking.repositories.LoanRepository;
import com.homebanking.Banking.services.ILoanService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author crowl
 */
@Service
public class LoanServiceImpl implements ILoanService{

    @Autowired
    private LoanRepository loanRepository;


    @Override
    public List<Loan> getAllLoan() {
        return loanRepository.findAll();
    }

    @Override
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).get();
    }

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }
    
}
