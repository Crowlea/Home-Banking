/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.homebanking.Banking.repositories;

import com.homebanking.Banking.entity.SecretKeyEnti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author crowl
 */
@Repository
public interface SecretKeyEntiRepository extends JpaRepository<SecretKeyEnti, Long>{
    
}
