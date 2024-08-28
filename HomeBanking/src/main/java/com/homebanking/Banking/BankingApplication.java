package com.homebanking.Banking;

import com.homebanking.Banking.security.Encrypted;
import com.homebanking.Banking.security.MasterKeyGenerator;
import java.security.NoSuchAlgorithmException;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankingApplication {

    @Autowired
    private Encrypted encry;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        SpringApplication.run(BankingApplication.class, args);
        System.out.println("Corriendo y Funcionando");
        System.out.println("http://localhost:8080/swagger-ui/index.html#/");
        

    }

}
