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
public class SalesResource extends Jooby {

    public SalesResource(SaleDAO dao) {

        path("api/sales", () -> {

            post((req, rsp) -> {
                Sale sale = req.body(Sale.class);

                // construct the URI for this sale
                String uri = "http://" + req.hostname() + ":" + req.port() + "" + req.path() + "/sale/" + sale.getId();

                // tell the product about its URI
                sale.setUri(uri);

                if (!dao.exists(sale.getId())) {
                    // store the product
                    dao.addSale(sale);

                    // return a response containing the complete product
                    rsp.status(Status.CREATED).send(sale);
                } else {
                    // Non-unique ID
                    rsp.status(Status.UNPROCESSABLE_ENTITY).send(new ErrorMessage("There is already a sale with that ID."));
                }

            });

        }).produces(MediaType.json).consumes(MediaType.json);
        
    }

}
