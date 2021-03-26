/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author zotta
 */
public class Summary {
    
    private Integer numberOfSales;
    private Double totalPayment;
    private String group;

    public Summary() {
    }

    public Summary(Integer numberOfSales, Double totalPayment, String group) {
        this.numberOfSales = numberOfSales;
        this.totalPayment = totalPayment;
        this.group = group;
    }

    @Override
    public String toString() {
        return "Summary{" + "numberOfSales=" + numberOfSales + ", totalPayment=" + totalPayment + ", group=" + group + '}';
    }
    
    
    
    
}
