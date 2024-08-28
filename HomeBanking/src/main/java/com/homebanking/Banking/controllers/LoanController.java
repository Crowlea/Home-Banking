/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homebanking.Banking.controllers;

import com.homebanking.Banking.entity.Account;
import com.homebanking.Banking.entity.Loan;
import com.homebanking.Banking.services.implement.AccountServiceImpl;
import com.homebanking.Banking.services.implement.LoanServiceImpl;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author crowl
 */
@RestController
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    LoanServiceImpl loanService;
    @Autowired
    AccountServiceImpl accountService;
   

    @GetMapping
    public ResponseEntity<?> getLoans() {
        List<Loan> loans = loanService.getAllLoan();
        if(loans.isEmpty()){
           return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(loans);
    }

    @Transactional
    @PostMapping("/saveloans")
    public ResponseEntity<?> createLoan(@RequestBody Loan loan) {

        if (loan != null ) {
            if(loan.getAmount().compareTo(loan.getMaxAmount())<0){
                if(loan.getPaymentsList().contains(loan.getPayments())){
                    Account destinationAccount=accountService.getAccountByNumber(loan.getDestinationAccoutn());
                    if(destinationAccount!=null){
                        destinationAccount.setAmount(destinationAccount.getAmount().add(loan.getAmount()));
                        loanService.saveLoan(loan);
                        return new ResponseEntity<>("Préstamo Aprobado", HttpStatus.CREATED);
                    }else{
                        return new ResponseEntity<>("La cuenta no Existe",HttpStatus.NOT_FOUND);
                    }
                }else{
                    return new ResponseEntity<>("Cuotas Incorrectas", HttpStatus.NOT_ACCEPTABLE);
                }
            }else{
                 return new ResponseEntity<>("Monto Execido", HttpStatus.NOT_ACCEPTABLE);
            }
        }else{
            return new ResponseEntity<>("Información Incorrecta", HttpStatus.FORBIDDEN);
        }
    }

}
