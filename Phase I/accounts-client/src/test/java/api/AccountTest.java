/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import domain.Account;
import java.io.IOException;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
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
public class AccountTest {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private AccountsApi accountsApi;
    private AccountApi accountApi;

    private Account account1;
    private Account account2;
    private Account account3;

    @BeforeEach
    public void setUp() throws IOException {

        accountsApi = retrofit.create(AccountsApi.class);
        accountApi = retrofit.create(AccountApi.class);

        account1 = new Account();
        account1.setId("id1");
        account1.setEmail("email1");
        account1.setUsername("username1");
        account1.setFirstName("firstname1");
        account1.setLastName("lastname1");
        account1.setGroup("group1");

        account2 = new Account();
        account2.setId("id2");
        account2.setEmail("email2");
        account2.setUsername("username2");
        account2.setFirstName("firstname2");
        account2.setLastName("lastname2");
        account2.setGroup("group2");

        account3 = new Account();
        account3.setId("id3");
        account3.setEmail("email3");
        account3.setUsername("username3");
        account3.setFirstName("firstname3");
        account3.setLastName("lastname3");
        account3.setGroup("group3");

        // POST account1 and account2 to service (leave account3 for other testing the POST method itself)
        accountsApi.createAccount(account1).execute();
        accountsApi.createAccount(account2).execute();

    }

    @AfterEach
    public void tearDown() throws IOException {
        accountApi.deleteAcount(account1.getId()).execute();
        accountApi.deleteAcount(account2.getId()).execute();
        accountApi.deleteAcount(account3.getId()).execute();

    }

    @Test
    public void deleteAccount() throws IOException {

        // GET the product that you are going to delete.
        Response<Account> getResponse = accountApi.getAccount(account2.getId()).execute();

        // check for 200 response
        assertThat(getResponse.code(), is(200));

        //delete the product
        Response getResponse2 = accountApi.deleteAcount(account2.getId()).execute();

        // check for 204 response
        assertThat(getResponse2.code(), is(204));

        // GET the product again to check if it was deleted.
        Response<Account> getResponse3 = accountApi.getAccount(account2.getId()).execute();
        // check for 404 response
        assertThat(getResponse3.code(), is(404));

    }

    /**
     * Test of updateAccount method, of class AccountApi.
     */
    @Test
    public void testUpdateAccount() throws IOException {

        // Change the name of a account.
        account1.setUsername("theUser");

        // PUT the account.
        Response getResponse = accountApi.updateAccount(account1.getId(), account1).execute();

        // check for 204 response
        assertThat(getResponse.code(), is(204));

        // GET the acccount.
        Response<Account> getResponse2 = accountApi.getAccount(account1.getId()).execute();

        // get the product from the response body
        Account returnedAccount = getResponse2.body();

        // check that the name property was properly set by service
        assertThat(returnedAccount, hasProperty("username", equalTo("theUser")));

        //Change the ID of a product.
        account1.setId("id666");

        // PUT the product.
        Response getResponse3 = accountApi.updateAccount("id1", account1).execute();

        // check for 409 response
        assertThat(getResponse3.code(), is(409));
    }

}
