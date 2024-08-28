/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homebanking.Banking.services.implement;

import com.homebanking.Banking.entity.Account;
import com.homebanking.Banking.entity.SecretKeyEnti;
import com.homebanking.Banking.entity.User;
import com.homebanking.Banking.repositories.AccountRepository;
import com.homebanking.Banking.security.KeyService;
import com.homebanking.Banking.services.IAccountService;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author crowl
 */
@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private KeyService keyService;

    @Override
    public List<Account> allAccount() {

        List<Account> allAccounts = accountRepository.findAll();
        List<Account> decriptedAccounts = new ArrayList();

        try {
            SecretKeyEnti secretKeyEnti = keyService.obtenerLlaveMaestra();
            SecretKey secretKey = new SecretKeySpec(secretKeyEnti.getEncryptedKey(), "AES");
            User user = new User();
            for (Account account : allAccounts) {
                Account decryptedAccount = new Account();
                decryptedAccount.setId(account.getId());
                decryptedAccount.setAmount(account.getAmount());
                decryptedAccount.setType(account.getType());

                decryptedAccount.setAlias(decryptString(account.getAlias(), secretKey));
                decryptedAccount.setCbu(decryptString(account.getCbu(), secretKey));
                decryptedAccount.setNumber(decryptString(account.getNumber(), secretKey));
                if (user.getId() != account.getOwner().getId()||user.getId() == null) {
                    user = userService.getDecryptedUser(account.getOwner().getId());
                    decryptedAccount.setOwner(user);
                } else {

                    decryptedAccount.setOwner(user);
                }

                System.out.println(user);
                decriptedAccounts.add(decryptedAccount);
            }

        } catch (Exception e) {
        }

        return decriptedAccounts;
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account save(Account account) {

        return accountRepository.save(account);
    }

    public void updateAccountEncrypted(Account account, Long id) {
        Account upddateAccount = getDecryptedAccount(id);
        if (upddateAccount != null) {
            upddateAccount.setAmount(account.getAmount());
            upddateAccount.setType(account.getType());
            upddateAccount.setAlias(account.getAlias());
            upddateAccount.setCbu(account.getCbu());
            upddateAccount.setNumber(account.getNumber());
            User user = userService.getDecryptedUser(account.getOwner().getId());
            upddateAccount.setOwner(user);

            saveEncryptedAccount(upddateAccount);
        }

    }

    @Override
    public Account getAccountByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    //Falta crear el atributo activo en la entidad
    @Override
    public void activateDeactivate(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void saveEncryptedAccount(Account accout) {
        try {
            SecretKeyEnti secretKeyEnti = keyService.obtenerLlaveMaestra(); // Suponiendo que la llave maestra está asociada al ID 1
            SecretKey secretKey = new SecretKeySpec(secretKeyEnti.getEncryptedKey(), "AES");

            // Cifrar cada campo individualmente
            String encryptedNumber = encryptString(accout.getNumber(), secretKey);
            String encryptedCBU = encryptString(accout.getCbu(), secretKey);
            String encryptedAlias = encryptString(accout.getAlias(), secretKey);
            // Actualizar el objeto Account con los campos cifrados
            accout.setAlias(encryptedAlias);
            accout.setCbu(encryptedCBU);
            accout.setNumber(encryptedNumber);

            accountRepository.save(accout);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Account getDecryptedAccount(Long accountId) {
        try {
            Account account = accountRepository.findById(accountId).orElse(null);
            if (account == null) {
                return null;
            }
            SecretKeyEnti secretKeyEnti = keyService.obtenerLlaveMaestra();
            SecretKey secretKey = new SecretKeySpec(secretKeyEnti.getEncryptedKey(), "AES");

            account.setAlias(decryptString(account.getAlias(), secretKey));
            account.setCbu(decryptString(account.getCbu(), secretKey));
            account.setNumber(decryptString(account.getNumber(), secretKey));
            User user = userService.getDecryptedUser(account.getOwner().getId());
            account.setOwner(user);

            return account;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

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

    @Override
    public Account updateAccount(Account account, Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
