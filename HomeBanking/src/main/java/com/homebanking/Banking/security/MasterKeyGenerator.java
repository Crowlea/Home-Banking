/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homebanking.Banking.security;

import com.homebanking.Banking.entity.SecretKeyEnti;
import com.homebanking.Banking.repositories.SecretKeyEntiRepository;
import jakarta.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 *
 * @author crowl
 */
@Component
public class MasterKeyGenerator {
 
   @Autowired
    private SecretKeyEntiRepository secretKeyRepository;

    public SecretKey generateMasterKey() {
        try {
            // Crear un generador de llaves AES
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

            // Inicializar el generador con un algoritmo seguro
            keyGenerator.init(128); // Tamaño de la llave (en bits)

            // Generar la llave maestra
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveMasterKeyToDatabase(SecretKey masterKey) {
        try {
            // Convertir la llave a un array de bytes
            byte[] keyBytes = masterKey.getEncoded();

            // Crear una entidad y guardarla en la base de datos
            SecretKeyEnti secretKeyEntity = new SecretKeyEnti();
            secretKeyEntity.setEncryptedKey(keyBytes);
            secretKeyRepository.save(secretKeyEntity);

            System.out.println("Llave maestra generada y guardada en la base de datos.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @PostConstruct
//    public void generateAndSaveMasterKey() {
//        // Generar la llave maestra
//        SecretKey masterKey = generateMasterKey();
//
//        // Guardar la llave en la base de datos
//        saveMasterKeyToDatabase(masterKey);
//    }
}
