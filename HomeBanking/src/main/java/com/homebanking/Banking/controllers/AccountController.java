/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homebanking.Banking.controllers;

import com.homebanking.Banking.entity.Account;
import com.homebanking.Banking.entity.User;
import com.homebanking.Banking.services.implement.AccountServiceImpl;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author crowl
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountServiceImpl accountService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Account> allAccounts = accountService.allAccount();

        if (allAccounts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allAccounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable Long id) {
        Account find = accountService.getDecryptedAccount(id);
        if (find == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(find);
    }

    @PostMapping
    public ResponseEntity<?> saveAccount(@RequestBody Account account) {
        accountService.saveEncryptedAccount(account);
        return ResponseEntity.ok("Cuenta guardado correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(@RequestBody Account account, @PathVariable Long id) {

        accountService.updateAccountEncrypted(account, id);

        return ResponseEntity.ok("Cuenta Actualizada Correctamente");
    }

}
