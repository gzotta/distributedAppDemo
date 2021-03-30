/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Account;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author zotta
 */
public class AccountDAO {
    
    private static final Map<String, Account> accounts = new TreeMap<>();
    
        /*
	 * Some dummy data for testing
     */
    static {
        if (accounts.isEmpty()) {
           accounts.put("accountid1", new Account("accountid1", "email1", "username1", "firstName1", "lastName1", "group1", "http://localhost:8080/api/accounts/account/accountid1"));
           accounts.put("accountid2", new Account("accountid2", "email2", "username2", "firstName2", "lastName2", "group2", "http://localhost:8080/api/accounts/account/accountid1"));
           
        }
    }
    
    public void createAccount(String accountId, Account account){
        accounts.put(accountId, account);
    }
    
    
    public void updateAccount(String accountId, Account updatedAccount){
        accounts.put(accountId, updatedAccount);
    
    }
    
    public List<Account> getAccounts(){
    return new ArrayList<>(accounts.values());
}
    
    public void deleteAccount(String accountId){
        accounts.remove(accountId);
    }
    

    public boolean exists(String accountId) {
        return accounts.containsKey(accountId);
    }
    
    public Account getAccount(String id){
        return accounts.get(id);
    }
    
}
