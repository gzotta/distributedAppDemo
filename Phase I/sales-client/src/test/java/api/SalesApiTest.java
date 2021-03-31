package api;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import domain.Customer;
import domain.Sale;
import domain.SaleItem;
import domain.Summary;
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
import retrofit2.Response;
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

    private Customer customerTest1;
    private Customer customerTest2;

    private static List<SaleItem> items;
    private static List<SaleItem> items2;
    private static Multimap<String, Sale> customerSales;

    @BeforeEach
    public void setUp() throws IOException {

        //These first two lines in the setUp method are telling Retrofit to 
        //create client-side stub objects that implement the two generated 
        //interfaces that contatain the API operations. 
        salesApi = retrofit.create(SalesApi.class);
        saleApi = retrofit.create(SaleApi.class);

        item1 = new SaleItem();
        item1.setProductId("productid1");
        item1.setQuantity(new BigDecimal("1.0"));
        item1.setPrice(new BigDecimal("11.11"));

        item2 = new SaleItem();
        item2.setProductId("productid2");
        item2.setQuantity(new BigDecimal("2.0"));
        item2.setPrice(new BigDecimal("5000.22"));

        items = new ArrayList<>();
        items2 = new ArrayList<>();

        items.add(item1);
        items2.add(item1);
        items2.add(item2);

        totals1 = new Totals();
        totals1.setTotalPrice(new BigDecimal("11.11"));
        totals1.setTotalTax(new BigDecimal("16.66"));
        totals1.setTotalPayment(new BigDecimal("12.11"));

        totals2 = new Totals();
        totals2.setTotalPrice(new BigDecimal("10000.12"));
        totals2.setTotalTax(new BigDecimal("16.66"));
        totals2.setTotalPayment(new BigDecimal("10000.12"));

        customerTest1 = new Customer();
        customerTest1.setId("customerid11");
        customerTest1.setEmail("email1");
        customerTest1.setGroup("Regular Customers");

        customerTest2 = new Customer();
        customerTest2.setId("customerid22");
        customerTest2.setEmail("email2");
        customerTest2.setGroup("Regular Customers");

        sale1 = new Sale();
        sale1.setId("saleid1");
        sale1.setSaleDate("06/06/2021");
        sale1.setCustomer(customerTest1);
        sale1.setItems(items);
        sale1.setTotals(totals1);

        sale2 = new Sale();
        sale2.setId("saleid2");
        sale2.setSaleDate("07/06/2021");
        sale2.setCustomer(customerTest2);
        sale2.setItems(items2);
        sale2.setTotals(totals2);

        customerSales = ArrayListMultimap.create();

        customerSales.put("customerid11", sale1);
        customerSales.put("customerid22", sale2);

        // POST sale1 to service (leave sale2 for other testing the POST method itself)
        salesApi.addSale(sale1).execute();
        //salesApi.addSale(sale2).execute();

    }

    @AfterEach
    public void tearDown() throws IOException {
        saleApi.delete(sale1.getId()).execute();
        saleApi.delete(sale2.getId()).execute();
    }

    /**
     * test addSale method of class salesApi.
     *
     * @throws IOException
     */
    @Test
    public void addSale() throws IOException {
        // call the method being tested
        Response<Sale> createResponse = salesApi.addSale(sale2).execute();

        // check for 201 response
        assertThat(createResponse.code(), is(201));

        // GET the sale that was created in the setUp() to check it was saved properly
        Response<Sale> getResponse = salesApi.getById(sale1.getId()).execute();

        // get the sale from the response body
        Sale returnedSale = getResponse.body();

        // check that returned sale has the correct properties (except for URI which was set by the service, so will be different)
        assertThat(returnedSale, samePropertyValuesAs(sale1, "uri"));

        // check that the URI property was properly set by service
        assertThat(returnedSale, hasProperty("uri", equalTo("http://localhost:8081/api/sales/sale/saleid1")));
        // call the method again - should get 422 response this time
        createResponse = salesApi.addSale(sale2).execute();
        assertThat(createResponse.code(), is(422));
    }

    @Test
    public void getByCustomer() throws IOException {
        // call the method being tested
        Response<List<Sale>> getResponse = salesApi.getByCustomer(customerTest1.getId()).execute();

        // get the products from the response
        List<Sale> returnedSales = getResponse.body();

        // check for 200 response
        assertThat(getResponse.code(), is(200));

        // the generated equals method includes the URI, so we need to set it if we want to compare products via hasItems
        sale1.setUri("http://localhost:8081/api/sales/sale/saleid1");

        // check that response includes both prod1 and prod2
        assertThat(returnedSales, hasItems(sale1));
    }

    @Test
    public void getSummary() throws IOException {
        // call the method being tested
        Response<Summary> getResponse = salesApi.getSummary(customerTest1.getId()).execute();

        // get the products from the response
        Summary returnedSummary = getResponse.body();

        // check for 200 response
        assertThat(getResponse.code(), is(200));

        // check that the URI property was properly set by service
        assertThat(returnedSummary, hasProperty("numberOfSales", equalTo(1)));
        assertThat(returnedSummary, hasProperty("totalPayment", equalTo(new BigDecimal("12.11"))));
        assertThat(returnedSummary, hasProperty("group", equalTo("Regular Customers")));
    }

}
