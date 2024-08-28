/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homebanking.Banking.controllers;

import com.homebanking.Banking.services.implement.TransferServiceImpl;
import com.homebanking.Banking.entity.Transfer;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author crowl
 */
@RestController
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    private TransferServiceImpl transferService;

    @GetMapping
    public ResponseEntity<?> allTransfer() {
        List<Transfer> transfer = transferService.allTransfer();
        if (transfer.isEmpty()) {

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transfer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> transferById(@PathVariable Long id) {
        Optional<Transfer> findTransfer = transferService.findById(id);
        if (findTransfer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(findTransfer);
    }

    @PostMapping("/performTransfer")
    public ResponseEntity<Transfer> performTransfer(@RequestParam Long originAccountId,
            @RequestParam Long destinationAccountId,
            @RequestParam BigDecimal amount) {
        Transfer transfer = transferService.performTransfer(originAccountId, destinationAccountId, amount);

        if (transfer != null) {
            return new ResponseEntity<>(transfer, HttpStatus.OK);
        } else {
            // Puedes personalizar el ResponseEntity según tu lógica.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
