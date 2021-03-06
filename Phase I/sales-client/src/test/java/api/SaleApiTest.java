/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import domain.Customer;
import domain.Sale;
import domain.SaleItem;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * @author zotta
 */
public class SaleApiTest {

    //The following code is what points Retrofit at our serives, and tells it to
    //convert sale objects to/from JSON via Gson library.
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:8081/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private SalesApi salesApi;
    private SaleApi saleApi;

    private Sale sale1;

    private Customer customer1;

    private SaleItem item1;
    private SaleItem item2;
    private List<SaleItem> itemsTest;

    @BeforeEach
    public void setUp() throws IOException {
        //These first two lines in the setUp method are telling Retrofit to 
        //create client-side stub objects that implement the two generated 
        //interfaces that contatain the API operations. 
        salesApi = retrofit.create(SalesApi.class);
        saleApi = retrofit.create(SaleApi.class);

        item1 = new SaleItem();
        item1.setPrice(new BigDecimal("7800.00"));
        item1.setProductId("productId1");
        item1.setQuantity(new BigDecimal("2.0"));

        item2 = new SaleItem();
        item2.setPrice(new BigDecimal("1800.00"));
        item2.setProductId("productId2");
        item2.setQuantity(new BigDecimal("1.0"));

        itemsTest = new ArrayList<>();
        itemsTest.add(item1);
        itemsTest.add(item2);

        customer1 = new Customer();
        customer1.setId("customerid1");
        customer1.setEmail("email1");
        customer1.setGroup("Regular customers");

        sale1 = new Sale();
        sale1.setItems(itemsTest);
        sale1.setId("id1");
        sale1.setCustomer(customer1);

        // POST sale1 to service 
        salesApi.addSale(sale1).execute();

    }

    @AfterEach
    public void tearDown() throws IOException {
        saleApi.delete(sale1.getId()).execute();
    }

    @Test
    public void delete() throws IOException {
        // GET the sale that you are going to delete.
        Response<Sale> getResponse = salesApi.getById(sale1.getId()).execute();

        // check for 200 response
        assertThat(getResponse.code(), is(200));

        //delete the sale
        Response getResponse2 = saleApi.delete(sale1.getId()).execute();

        // check for 204 response
        assertThat(getResponse2.code(), is(204));

        // GET the product again to check if it was deleted.
        Response<Sale> getResponse3 = salesApi.getById(sale1.getId()).execute();
        // check for 404 response
        assertThat(getResponse3.code(), is(404));

    }

}
