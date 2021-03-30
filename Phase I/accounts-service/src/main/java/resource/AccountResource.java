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
public class AccountResource extends Jooby {

    public AccountResource(AccountDAO dao) {

        path("/api/accounts/account", () -> {

            // A route that sits at the top of the chain that checks that the ID
            // is valid so that the other routes don't need to.
            use("/:id", (req, rsp, chain) -> {
                String id = req.param("id").value();

                if (dao.exists(id)) {
                    // ID is OK, so pass request on to the next route in the chain
                    chain.next(req, rsp);
                } else {
                    // Bad ID - send a 404 response
                    rsp.status(Status.NOT_FOUND).send(new ErrorMessage("There is no account matching that ID."));
                }
            });

            
            // Get an account by its ID
            get("/:id", (req) -> {
                String id = req.param("id").value();
                return dao.getAccount(id);
            });

            
            put("/:id", (req, rsp) -> {
                String id = req.param("id").value();
                Account account = req.body().to(Account.class);

                if (!id.equals(account.getId())) {
                    rsp.status(Status.CONFLICT).send(new ErrorMessage("Modifying the account ID via this operation is not allowed."));
                } else {
                    dao.updateAccount(id, account);
                    rsp.status(Status.NO_CONTENT);
                }
            });

            delete("/:id", (req, rsp) -> {
                String id = req.param("id").value();
                dao.deleteAccount(id);
                rsp.status(Status.NO_CONTENT);
            });

        }).produces(MediaType.json).consumes(MediaType.json);

    }
}
