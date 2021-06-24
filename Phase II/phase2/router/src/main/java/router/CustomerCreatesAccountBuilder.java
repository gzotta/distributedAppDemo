/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package router;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 *
 * @author zotta
 */
public class CustomerCreatesAccountBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        //embbeded instance of Jetty that receive the account details from AJAX client.
        from("jetty:http://localhost:9000/messages?enableCORS=true")
                // make message in-only so web browser doesn't have to wait on a non-existent response
                .setExchangePattern(ExchangePattern.InOnly)
                .convertBodyTo(String.class)
                .log("${body}")
                .to("jms:queue:jetty-in");

        
        //adding the Vend endpoint and POSTing the customer to Vend API (fake htttp first to test the route).
        from("jms:queue:jetty-in")
                // remove headers so they don't get sent to Vend
                .removeHeaders("*")
                // add authentication token to authorization header
                .setHeader("Authorization", constant("Bearer KiQSsELLtocyS2WDN5w5s_jYaBpXa0h2ex1mep1a"))
                // marshal to JSON
                //.marshal().json(JsonLibrary.Gson) // only necessary if the message is an object, not JSON
                .setHeader(Exchange.CONTENT_TYPE).constant("application/json")
                // set HTTP method
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                // send it
                .to("https://info303otago.vendhq.com/api/2.0/customers?throwExceptionOnFailure=false")
                // handle response
                .choice()
                .when().simple("${header.CamelHttpResponseCode} == '201'") // change to 200 for PUT
                .to("jms:queue:vend-response")
                .otherwise()
                .log("ERROR RESPONSE ${header.CamelHttpResponseCode} ${body}")
                .convertBodyTo(String.class)
                .to("jms:queue:vend-error")
                .endChoice();

        //extract Customer in JSON form
        from("jms:queue:vend-response")
                .setBody().jsonpath("$.data")
                .marshal().json(JsonLibrary.Gson)
                .to("jms:queue:extracted-response");

        //adding the REST services from phase I endpoint and POSTing the account.
        from("jms:queue:extracted-response")
                //.marshal().json(JsonLibrary.Gson) // only necessary if object needs to be converted to JSON
                .removeHeaders("*") // remove headers to stop them being sent to the service
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("http://localhost:8086/api/accounts")
                .to("jms:queue:ca-http-response");  // HTTP response ends up in this queue

    }

}
