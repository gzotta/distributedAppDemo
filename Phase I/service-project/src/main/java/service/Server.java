package service;

import java.io.IOException;
import org.jooby.Jooby;
import org.jooby.handlers.Cors;
import org.jooby.handlers.CorsHandler;
import org.jooby.json.Gzon;

public class Server extends Jooby {

	public Server() {

		// the port that the service will run on (should be different for each service)
		port(8081);

		// add CORS support so the client can access the operations
		use("*", new CorsHandler(new Cors().withMethods("*")));

		// encode message bodies as JSON
		use(new Gzon());

	}

	public static void main(String[] args) throws IOException {
		// start the service
		new Server().start();
	}

}
