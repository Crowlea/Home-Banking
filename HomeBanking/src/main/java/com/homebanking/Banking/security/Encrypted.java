/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homebanking.Banking.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author crowl
 */
@Component
public class Encrypted {


    
    // Método para cifrar una cadena de texto
    private String encryptString(String text, SecretKey secretKey) throws Exception {
        // Inicializar el cifrado con el algoritmo y modo adecuados
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Generar un vector de inicialización (IV) aleatorio
        SecureRandom random = new SecureRandom();
        byte[] ivBytes = new byte[cipher.getBlockSize()];
        random.nextBytes(ivBytes);
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        // Inicializar el cifrado con la clave y el IV
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        // Cifrar la cadena de texto
        byte[] encryptedBytes = cipher.doFinal(text.getBytes("UTF-8"));

        // Concatenar el IV con los datos cifrados
        byte[] combinedBytes = new byte[ivBytes.length + encryptedBytes.length];
        System.arraycopy(ivBytes, 0, combinedBytes, 0, ivBytes.length);
        System.arraycopy(encryptedBytes, 0, combinedBytes, ivBytes.length, encryptedBytes.length);

        // Devolver el resultado cifrado como una cadena codificada en Base64
        return Base64.getEncoder().encodeToString(combinedBytes);
    }

    // Método para descifrar una cadena de texto
    public String decryptString(String encryptedText, SecretKey secretKey) throws Exception {
        // Decodificar los datos cifrados desde Base64
        byte[] combinedBytes = Base64.getDecoder().decode(encryptedText);

        // Extraer el IV de los datos cifrados
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] ivBytes = Arrays.copyOfRange(combinedBytes, 0, cipher.getBlockSize());
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        // Inicializar el cifrado con la clave y el IV
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        // Descifrar los datos
        byte[] decryptedBytes = cipher.doFinal(combinedBytes, cipher.getBlockSize(), combinedBytes.length - cipher.getBlockSize());

        // Convertir los bytes descifrados de vuelta a la cadena de texto original
        return new String(decryptedBytes, "UTF-8");
    }

}
