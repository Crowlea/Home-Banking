/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.homebanking.Banking.services;

import com.homebanking.Banking.entity.User;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author crowl
 */
public interface IUserService {
    
    public List<User>allUser();
    public Optional<User>findById(Long id);
    public User save(User user);
    public void activateDeactivate(Long id);
    
    
}
