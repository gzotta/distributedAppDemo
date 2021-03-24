/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.Map;

/**
 *
 * @author zotta
 */
public class Sale {

    private String id;
    private String saleDate;
    private Customer customer;
    private Map<String, SaleItem> items;
    private Totals totals;
    private String uri;

    public Sale() {
    }

    public Sale(String id, String saleDate, Customer customer, Map<String, SaleItem> items, Totals totals, String uri) {
        this.id = id;
        this.saleDate = saleDate;
        this.customer = customer;
        this.items = items;
        this.totals = totals;
        this.uri = uri;
    }


    
    
    
}
