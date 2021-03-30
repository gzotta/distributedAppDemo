package api;

import domain.Account;
import java.io.IOException;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountsTest {

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
    public void createAccount() throws IOException {
        // call the method being tested
        Response<Account> createResponse = accountsApi.createAccount(account3).execute();

        // check for 201 response
        assertThat(createResponse.code(), is(201));

        // call the method again - should get 422 response this time
        createResponse = accountsApi.createAccount(account3).execute();
        assertThat(createResponse.code(), is(422));
    }

    @Test
    public void getAccounts() throws IOException {

        // call the method being tested
        Response<List<Account>> getResponse = accountsApi.getAccounts().execute();

        // get the products from the response
        List<Account> returnedAccounts = getResponse.body();

        // check for 200 response
        assertThat(getResponse.code(), is(200));

        // the generated equals method includes the URI, so we need to set it if we want to compare products via hasItems
        account1.setUri("http://localhost:8080/api/accounts/account/id1");
        account2.setUri("http://localhost:8080/api/accounts/account/id2");

        // check that response includes both prod1 and prod2
        assertThat(returnedAccounts, hasItems(account1, account2));

    }

}
