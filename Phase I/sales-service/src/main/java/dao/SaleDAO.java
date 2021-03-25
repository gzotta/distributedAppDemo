package dao;

import domain.Customer;
import domain.Sale;
import domain.SaleItem;
import domain.Totals;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author zotta
 */
public class SaleDAO {

    private static final Map<String, Sale> sales = new TreeMap<>();

    /*
	 * Some dummy data for testing
     */
    static {
        if (sales.isEmpty()) {
            Map<String, SaleItem> items = new TreeMap<>();
            items.put("prodId1", new SaleItem("prodId1", 1.0, 11.0));
            items.put("prodId2", new SaleItem("prodId2", 2.0, 12.0));
            sales.put("id1", new Sale("id1", "01/02/21", new Customer("customerid1", "email1", "group1"), items, new Totals(111.0, 0.16, 111.16), "http://localhost:8080/api/sales/id1"));
            sales.put("id2", new Sale("id2", "02/02/21", new Customer("customerid2", "email2", "group2"), items, new Totals(112.0, 0.16, 112.16), "http://localhost:8080/api/sales/id2"));
        }
    }
    
    
    	/**
	 * Adds a new sale
	 *
	 * @param sale The sale being added.
	 */
	public void addSale(Sale sale) {
		sales.put(sale.getId(), sale);
	}
        
        


}
