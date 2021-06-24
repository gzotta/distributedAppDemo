/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import domain.Account;

/**
 *
 * @author zotta
 */
public class CreateAccount {
    
    public Account createAccount(String id, String email, String username, String firstName, String lastName, String group){
        return new Account(id, email, username, firstName, lastName, group);
        
    }
    
}
