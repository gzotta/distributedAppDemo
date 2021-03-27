/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import dao.SaleDAO;
import domain.ErrorMessage;
import domain.Sale;
import org.jooby.Jooby;
import org.jooby.MediaType;
import org.jooby.Status;

/**
 *
 * @author zotta
 */
public class CustomerResource extends Jooby {

    public CustomerResource(SaleDAO dao) {

        path("/api/Sales/sale/{id}/customer/", () -> {

            // A route that sits at the top of the chain that checks that the ID
            // is valid so that the other routes don't need to.
            use("/:id", (req, rsp, chain) -> {
                String id = req.param("id").value();

                if (dao.exists(id)) {
                    // ID is OK, so pass request on to the next route in the chain
                    chain.next(req, rsp);
                } else {
                    // Bad ID - send a 404 response
                    rsp.status(Status.NOT_FOUND).send(new ErrorMessage("There is no sale matching that ID."));
                }
            });

            //Get all sales for a specific customer.
            get("/:id", (req) -> {
                String id = req.param("id").value();
                return dao.getByCustomer(id);

            });

            //Get a customer's sales summary.
            get("/:id", (req) -> {
                String id = req.param("id").value();
                return dao.getSummary(id);
            });

        }).produces(MediaType.json).consumes(MediaType.json);
    }
}
