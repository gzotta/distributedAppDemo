package service;

import dao.SaleDAO;
import java.io.IOException;
import org.jooby.Jooby;
import org.jooby.handlers.Cors;
import org.jooby.handlers.CorsHandler;
import org.jooby.json.Gzon;
import resource.CustomersResource;
import resource.SaleResource;
import resource.SalesResource;

public class Server extends Jooby {

    public Server() {

// add CORS support so the client can access the operations
        use("*", new CorsHandler(new Cors().withMethods("*")));

// the port that the service will run on (should be different for each service)
        port(8081);

        SaleDAO dao = new SaleDAO();

        // encode message bodies as JSON
        use(new Gzon());

        use(new SalesResource(dao));
        use(new SaleResource(dao));
        use(new CustomersResource(dao));

    }

    public static void main(String[] args) throws IOException {
        // start the service
        new Server().start();
    }

}
