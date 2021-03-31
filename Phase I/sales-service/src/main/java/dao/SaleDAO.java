package dao;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import domain.Customer;
import domain.Sale;
import domain.SaleItem;
import domain.Summary;
import domain.Totals;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author zotta
 */
public class SaleDAO {

    private static final Map<String, Sale> sales = new TreeMap<>();
    private static final Multimap<String, Sale> customerSales = ArrayListMultimap.create();

    /*
	 * Some dummy data for testing
     */
    static {
        if (sales.isEmpty()) {
            List<SaleItem> items1 = new ArrayList<>();
            List<SaleItem> items2 = new ArrayList<>();
            SaleItem item1 = new SaleItem("prodId1", 1.0, 11.0);
            SaleItem item2 = new SaleItem("prodId2", 2.0, 12.0);
            items1.add(item1);
            items1.add(item2);
            items2.add(item2);
            Customer customer1 = new Customer("customerid1", "email1", "group1");
            Customer customer2 = new Customer("customerid2", "email2", "group2");
            Sale sale1 = new Sale("id1", "01/02/21", customer1, items1, new Totals(111.0, 0.16, 111.16), "http://localhost:8081/api/sales/sale/id1");
            Sale sale2 = new Sale("id2", "02/02/21", customer2, items2, new Totals(6000.0, 0.16, 6000.16), "http://localhost:8081/api/sales/sale/id2");
            Sale sale3 = new Sale("id3", "03/03/21", customer1, items2, new Totals(112.0, 0.16, 112.16), "http://localhost:8081/api/sales/sale/id3");
            sales.put("id1", sale1);
            sales.put("id2",sale2 );
            sales.put("id3", sale3);
            customerSales.put("customerid1",sale1 );
            customerSales.put("customerid2",sale2 );
            customerSales.put("customerid1",sale3 );
        }
    }

    /**
     * Adds a new sale
     *
     * @param sale The sale being added.
     */
    public void addSale(Sale sale) {
        sales.put(sale.getId(), sale);
        customerSales.put(sale.getCustomer().getId(), sale);
    }
    
    // Get a sale by the sale's ID.
    public Sale getById(String saleId){
        return sales.get(saleId);
    }
    

    // Get all sales for a specific customer.
    public List<Sale> getByCustomer(String customerId) {
        return new ArrayList<>(customerSales.get(customerId));
    }

    // Get a summary of the sale data for a specific customer.
    public Summary getSummary(String clientId) {
        Integer numOfSales = 0;
        Double totPayment = 0.0;
        String group = "Regular Customers";
        List<Sale> salesById = getByCustomer(clientId);
        for (Sale sale : salesById) {
            numOfSales++;
            totPayment += sale.getTotals().getTotalPayment();
        }
        if (totPayment >= 5000.0) {
            group = "VIP Customers";
        }

        Summary clientSummary = new Summary(numOfSales, totPayment, group);
        return clientSummary;
    }

    // Delete a sale.
    public void delete(String saleId, Sale sale) {
        sales.remove(saleId);
        customerSales.remove(sale.getCustomer().getId(), sale);
    }

    /**
     * Checks if a sale exists.
     *
     * @param id The ID of the sale being checked.
     * @return <code>true</code> if product exists, <code>false</code> if not.
     */
    public boolean exists(String id) {
        return sales.containsKey(id);
    }
    
    
        /**
     * Checks if a customer exists.
     *
     * @param id The ID of the customer being checked.
     * @return <code>true</code> if product exists, <code>false</code> if not.
     */
    public boolean customerExists(String id) {
        return customerSales.containsKey(id);
    }
    
    

}
