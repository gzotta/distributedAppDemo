/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.List;
import java.util.Map;

/**
 *
 * @author zotta
 */
public class Sale {

    private String id;
    private String saleDate;
    private Customer customer;
    private List<SaleItem>items;
    private Totals totals;
    private String uri;

    public Sale() {
    }

    public Sale(String id, String saleDate, Customer customer, List<SaleItem> items, Totals totals, String uri) {
        this.id = id;
        this.saleDate = saleDate;
        this.customer = customer;
        this.items = items;
        this.totals = totals;
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public void setItems(List<SaleItem> items) {
        this.items = items;
    }

    public Totals getTotals() {
        return totals;
    }

    public void setTotals(Totals totals) {
        this.totals = totals;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


    
    
    
}
