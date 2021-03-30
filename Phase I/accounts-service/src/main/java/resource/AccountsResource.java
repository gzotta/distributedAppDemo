/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import dao.AccountDAO;
import domain.Account;
import domain.ErrorMessage;
import org.jooby.Jooby;
import org.jooby.MediaType;
import org.jooby.Status;

/**
 *
 * @author zotta
 */
public class AccountsResource extends Jooby {

    public AccountsResource(AccountDAO dao) {

        path(
                "/api/accounts", () -> {

                    //Get all registered customer accounts.
                    get(() -> {
                        return dao.getAccounts();
                    });

                    post((req, rsp) -> {
                        Account account = req.body(Account.class);

                        // construct the URI for this product
                        String uri = "http://" + req.hostname() + ":" + req.port() + "" + req.path() + "/account/" + account.getId();

                        // tell the product about its URI
                        account.setUri(uri);

                        if (!dao.exists(account.getId())) {
                            // store the product
                            dao.createAccount(account.getId(), account);

                            // return a response containing the complete product
                            rsp.status(Status.CREATED).send(account);
                        } else {
                            // Non-unique ID
                            rsp.status(Status.UNPROCESSABLE_ENTITY).send(new ErrorMessage("There is already an account with that ID."));
                        }

                    });

                }
        ).produces(MediaType.json)
                .consumes(MediaType.json);

    }
}
