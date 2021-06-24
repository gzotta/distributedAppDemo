/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import domain.Customer;

/**
 *
 * @author zotta
 */
public class CreateCustomer {
    
    public Customer createCustomer(String id, String group, String email, String firstName, String lastName){
        return new Customer(id, group,  email, firstName,  lastName);
    }
    
    
}
