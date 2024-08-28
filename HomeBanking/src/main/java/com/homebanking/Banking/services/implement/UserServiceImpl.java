/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.homebanking.Banking.services.implement;

import com.homebanking.Banking.entity.SecretKeyEnti;
import com.homebanking.Banking.entity.User;
import com.homebanking.Banking.repositories.UserRepository;
import com.homebanking.Banking.security.KeyService;
import com.homebanking.Banking.services.IUserService;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Period;
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
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KeyService keyService;

    @Override
    public List<User> allUser() {
        List<User> userList = userRepository.findAll();
        List<User> decryptedUserList = new ArrayList<>();

        try {
            // Obtener la llave maestra para descifrar los datos
            SecretKeyEnti secretKeyEnti = keyService.obtenerLlaveMaestra(); // Suponiendo que la llave maestra está asociada al ID 1
            SecretKey secretKey = new SecretKeySpec(secretKeyEnti.getEncryptedKey(), "AES");

            // Iterar sobre cada usuario recuperado de la base de datos
            for (User user : userList) {
                // Descifrar los campos cifrados de cada usuario
                User decryptedUser = new User();
                decryptedUser.setId(user.getId());
                decryptedUser.setEmail(decryptString(user.getEmail(), secretKey));
                decryptedUser.setPassword(decryptString(user.getPassword(), secretKey));
                decryptedUser.setName(decryptString(user.getName(), secretKey));
                decryptedUser.setSurname(decryptString(user.getSurname(), secretKey));
                decryptedUser.setDni(decryptString(user.getDni(), secretKey));
                decryptedUser.setAddress(decryptString(user.getAddress(), secretKey));
                decryptedUser.setYearOld(decryptString(user.getYearOld(), secretKey));
                decryptedUser.setGender(decryptString(user.getGender(), secretKey));
                decryptedUser.setDateOfBirth(user.getDateOfBirth());

                // Agregar el usuario desencriptado a la lista resultante
                decryptedUserList.add(decryptedUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar cualquier excepción que pueda ocurrir durante el proceso de descifrado
            return null;
        }

        return decryptedUserList;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    //Faltaria Agregar el atributo activo para poder implementarlo
    @Override
    public void activateDeactivate(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Método para guardar un usuario cifrado en la base de datos
    public void saveEncryptedUser(User user) {
        try {
            SecretKeyEnti secretKeyEnti = keyService.obtenerLlaveMaestra(); // Suponiendo que la llave maestra está asociada al ID 1
            SecretKey secretKey = new SecretKeySpec(secretKeyEnti.getEncryptedKey(), "AES");

            // Cifrar cada campo individualmente
            String encryptedEmail = encryptString(user.getEmail(), secretKey);
            String encryptedPassword = encryptString(user.getPassword(), secretKey);
            String encryptedName = encryptString(user.getName(), secretKey);
            String encryptedSurname = encryptString(user.getSurname(), secretKey);
            String encryptedDni = encryptString(user.getDni(), secretKey);
            String encryptedAddress = encryptString(user.getAddress(), secretKey);
            String encryptedGender = encryptString(user.getGender().toString(), secretKey);

            //Calculo la edad dependiendo de la fecha de naciemiento
            LocalDate now = LocalDate.now();
            Integer yearOld = Period.between(user.getDateOfBirth(), now).getYears();
            //Convertir el Integer a una cadena y luego encriptarla
            String yearOldString = yearOld.toString();
            String encryptedYearOld = encryptString(yearOldString, secretKey);

            //Actualizar el objeto User con los campos cifrados
            user.setEmail(encryptedEmail);
            user.setPassword(encryptedPassword);
            user.setName(encryptedName);
            user.setSurname(encryptedSurname);
            user.setDni(encryptedDni);
            user.setYearOld(encryptedYearOld);
            user.setAddress(encryptedAddress);
            user.setGender(encryptedGender);

            // Guardar el usuario en la base de datos
            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar cualquier excepción que pueda ocurrir durante el proceso de cifrado y guardado
        }
    }

    // Método para obtener un usuario y descifrar sus datos
    public User getDecryptedUser(Long userId) {
        try {
            // Obtener el usuario de la base de datos
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return null; // Manejar el caso donde el usuario no se encuentra en la base de datos
            }

            SecretKeyEnti secretKeyEnti = keyService.obtenerLlaveMaestra(); // Suponiendo que la llave maestra está asociada al ID 1
            SecretKey secretKey = new SecretKeySpec(secretKeyEnti.getEncryptedKey(), "AES");

            // Descifrar cada campo individualmente
            user.setEmail(decryptString(user.getEmail(), secretKey));
            user.setPassword(decryptString(user.getPassword(), secretKey));
            user.setName(decryptString(user.getName(), secretKey));
            user.setSurname(decryptString(user.getSurname(), secretKey));
            user.setDni(decryptString(user.getDni(), secretKey));
            user.setAddress(decryptString(user.getAddress(), secretKey));
            user.setYearOld(decryptString(user.getYearOld(), secretKey));
            user.setGender(decryptString(user.getGender(), secretKey));

            return user;
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar cualquier excepción que pueda ocurrir durante el proceso de descifrado
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
}
