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
public class SaleResource extends Jooby {

    public SaleResource(SaleDAO dao) {

        path("/api/Sales/sale/", () -> {

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

            //Get a sale by customer ID.
            get("/:id", (req) -> {
                String id = req.param("id").value();
                //Sale sale = req.body().to(Sale.class);
                Sale sale = dao.getById(id);
                String customerId = sale.getCustomer().getId();
                return dao.getByCustomer(customerId);

            });

            
            //Get a customer's sales summary.
            get("/:id", (req) -> {
                String id = req.param("id").value();
                return dao.getSummary(id);
            });

            
            //Delete a sale. 
            delete("/:id", (req, rsp) -> {
                String id = req.param("id").value();
                Sale sale = dao.getById(id);
                dao.delete(id, sale);
                rsp.status(Status.NO_CONTENT);
            });

            }).produces(MediaType.json).consumes(MediaType.json);
        }
    }
