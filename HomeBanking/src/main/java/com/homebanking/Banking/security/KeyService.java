/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homebanking.Banking.security;

import com.homebanking.Banking.entity.SecretKeyEnti;
import com.homebanking.Banking.repositories.SecretKeyEntiRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author crowl
 */
@Service
public class KeyService {
    
    @Autowired
    private SecretKeyEntiRepository secretKeyRepository;

    

     public SecretKeyEnti obtenerLlaveMaestra() {
        Optional<SecretKeyEnti> optionalSecretKey = secretKeyRepository.findById(1L);

        if (optionalSecretKey.isPresent()) {
            return optionalSecretKey.get();
        } else {
            // Manejar el caso donde no se encuentra la llave maestra en la base de datos
            return null;
        }
    }
    
}
