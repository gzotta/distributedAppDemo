package api;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import domain.Customer;
import domain.Sale;
import domain.SaleItem;
import domain.Totals;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SalesApiTest {

    //The following code is what points Retrofit at our serives, and tells it to
    //convert sale objects to/from JSON via Gson library.
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:8081/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private SalesApi salesApi;
    private SaleApi saleApi;

    private SaleItem item1;
    private SaleItem item2;

    private Totals totals1;
    private Totals totals2;

    private Sale sale1;
    private Sale sale2;

    private Customer customer1;
    private Customer customer2;

    private static final List<SaleItem> items = new ArrayList<>();
    private static final List<SaleItem> items2 = new ArrayList<>();
    private Multimap<String, Sale> customerSales = ArrayListMultimap.create();

    @BeforeEach
    public void setUp() throws IOException {

        //These first two lines in the setUp method are telling Retrofit to 
        //create client-side stub objects that implement the two generated 
        //interfaces that contatain the API operations. 
        salesApi = retrofit.create(SalesApi.class);
        saleApi = retrofit.create(SaleApi.class);

        item1 = new SaleItem();
        item1.setProductId("productid1");
        item1.setQuantity(new BigDecimal(1));
        item1.setPrice(new BigDecimal(11.11));

        item2 = new SaleItem();
        item2.setProductId("productid2");
        item2.setQuantity(new BigDecimal(2));
        item2.setPrice(new BigDecimal(5000.22));

        items.add(item1);
        items2.add(item1);
        items2.add(item2);

        totals1 = new Totals();
        totals1.setTotalPrice(new BigDecimal(11.11));
        totals1.setTotalTax(new BigDecimal(16.6));
        totals1.setTotalPayment(new BigDecimal(12.11));

        totals2 = new Totals();
        totals2.setTotalPrice(new BigDecimal(10000.12));
        totals2.setTotalTax(new BigDecimal(16.6));
        totals2.setTotalPayment(new BigDecimal(10000.12));

        customer1 = new Customer();
        customer1.setId("customerid1");
        customer1.setEmail("email1");
        customer1.setGroup("Regular Customers");

        customer2 = new Customer();
        customer2.setId("customerid2");
        customer2.setEmail("email2");
        customer2.setGroup("Regular Customers");

        sale1 = new Sale();
        sale1.setId("saleid1");
        sale1.setCustomer(customer1);
        sale1.setSaleDate("06/06/2021");
        sale1.setItems(items);
        sale1.setTotals(totals1);

        sale2 = new Sale();
        sale2.setId("saleid2");
        sale2.setCustomer(customer2);
        sale2.setSaleDate("07/06/2021");
        sale2.setItems(items2);
        sale2.setTotals(totals2);

        customerSales.put("customerid1", sale1);
        customerSales.put("customerid2", sale2);

        // POST sale1 to service (leave sale2 for other testing the POST method itself)
        salesApi.addSale(sale1).execute();
        //salesApi.addSale(sale2).execute();

    }

    @AfterEach
    public void tearDown() throws IOException {
        saleApi.delete(sale1.getId()).execute();
    }

    
    
    /**
     * test addSale method of class salesApi.
     * @throws IOException 
     */
    @Test
    public void addSale() throws IOException {
        fail("Test is not implemented yet.");
    }

    @Test
    public void getByCustomer() throws IOException {
        fail("Test is not implemented yet.");
    }

    
       @Test
    public void getSummary() throws IOException {
        fail("Test is not implemented yet.");
    }
    
    
}
