/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package router;

import domain.Account;
import domain.Sale;
import domain.Summary;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import util.CreateAccount;
import util.CreateCustomer;
import util.GroupConverter;

/**
 *
 * @author zotta
 */
public class CustomerMakesAsaleBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        //convert the new sale payload from JSON to object
        from("jms:queue:new-sale")
                // convert JSON to domain object
                .unmarshal().json(JsonLibrary.Gson, Sale.class)
                .to("jms:queue:new-sale-object");

        //Extract necessary fields from the sale object
        //Note: because the payload is coming in a Java object, use Java domain classes field's names not JSON's.
        from("jms:queue:new-sale-object")
                .setProperty("group_id").simple("${body.customer.group}") // extract group ID 
                .setProperty("id").simple("${body.customer.id}") // extract customer's ID 
                .setProperty("email").simple("${body.customer.email}") // extract email 
                .setProperty("first_name").simple("${body.customer.firstName}") // extract first name 
                .setProperty("last_name").simple("${body.customer.lastName}") // extract lastname 
                .log("customer group id: ${body.customer.group}")
                .log("customer id: ${body.customer.id}")
                .log("email: ${body.customer.email}")
                .log("first name: ${body.customer.firstName}")
                .log("last name: ${body.customer.lastName}")
                .to("jms:queue:saleExtractedFields");

        //POST the sale to the REST service from phase I
        from("jms:queue:saleExtractedFields")
                .marshal().json(JsonLibrary.Gson) // only necessary if object needs to be converted to JSON
                .removeHeaders("*") // remove headers to stop them being sent to the service
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("customer id2: ${exchangeProperty.id}")
                .to("http://localhost:8081/api/sales")
                .to("jms:queue:http-restPahseI-response");  // HTTP response ends up in this queue

        //Extract the sale's summary
        from("jms:queue:http-restPahseI-response")
                .removeHeaders("*")
                .setBody(constant(null)) // can't pass a body in a GET request
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("customer id3: ${exchangeProperty.id}")
                .toD("http://localhost:8081/api/sales/customer/${exchangeProperty.id}/summary")
                .to("jms:queue:summary");

        //convert summary into Object
        from("jms:queue:summary")
                // convert JSON to domain object
                .unmarshal().json(JsonLibrary.Gson, Summary.class)
                .log("Summary Object: ${body}")
                .setProperty("group").simple("${body.group}")
                .log("group: ${body.group}")
                .to("jms:queue:summary-object");

        //Convert the customer group text into customer group code using bean
        from("jms:queue:summary-object")
                .bean(GroupConverter.class, "convert(${exchangeProperty.group})")
                .to("jms:queue:groupCode");

        //Compare the customer's calcualted group with the current one.
        from("jms:queue:groupCode")
                .log("Group body: ${body}")
                .choice()
                .when().simple("${body} == ${exchangeProperty.group_id}")
                .to("jms:queue:groupUnchanged")
                .otherwise()
                .to("jms:queue:groupToUpdate");

        //Use bean to create the customer object with updated group id
        from("jms:queue:groupToUpdate")
                .bean(CreateCustomer.class, "createCustomer(${exchangeProperty.id},${body}, ${exchangeProperty.email}, "
                        + "${exchangeProperty.first_name}, ${exchangeProperty.last_name})")
                .to("jms:queue:customerObject");

        //PUT the updated customer into customers Vend service from phase I 
        from("jms:queue:customerObject")
                .log("id for vend PUT uri: ${exchangeProperty.id}")
                .log("account object: ${body}")
                // remove headers so they don't get sent to Vend
                .removeHeaders("*")
                // add authentication token to authorization header
                .setHeader("Authorization", constant("Bearer KiQSsELLtocyS2WDN5w5s_jYaBpXa0h2ex1mep1a"))
                // marshal to JSON
                .marshal().json(JsonLibrary.Gson) // only necessary if the message is an object, not JSON
                .setHeader(Exchange.CONTENT_TYPE).constant("application/json")
                // set HTTP method
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                // send it
                .toD("https://info303otago.vendhq.com/api/2.0/customers/${exchangeProperty.id}?throwExceptionOnFailure=false")
                // handle response
                .choice()
                .when().simple("${header.CamelHttpResponseCode} == '200'") // change to 200 for PUT
                .to("jms:queue:vend-response")
                .otherwise()
                .log("ERROR RESPONSE ${header.CamelHttpResponseCode} ${body}")
                .convertBodyTo(String.class)
                .to("jms:queue:vend-error")
                .endChoice();
        
        

        //extract Customer in JSON format from Vend resoponse (have to extract because it is in a wrapped object "data")
        from("jms:queue:vend-response")
                .setBody().jsonpath("$.data")
                .marshal().json(JsonLibrary.Gson)
                .to("jms:queue:extracted-response");


        //convert Vend response into Account Object
        from("jms:queue:extracted-response")
                // convert JSON to domain object
                .unmarshal().json(JsonLibrary.Gson, Account.class)
                .to("jms:queue:vend-response-object");

        //Use bean to create the account object with updated group id
        from("jms:queue:vend-response-object")
                .bean(CreateAccount.class, "createAccount(${exchangeProperty.id},${exchangeProperty.email}, ${exchangeProperty.first_name},"
                        + "${exchangeProperty.first_name}, ${exchangeProperty.last_name}, ${body.group})")
                .to("jms:queue:accountObject");

        //PUT the account into customer accounts service from phase I
        from("jms:queue:accountObject")
                .log("ID for PUT last route: ${exchangeProperty.id}")
                .marshal().json(JsonLibrary.Gson) // only necessary if object needs to be converted to JSON
                .setProperty("fizzjigID").header("fizzjigID") // copy resource ID to property (since we are about the delete the headers)
                .removeHeaders("*") // remove headers to stop them being sent to the service
                .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .toD("http://localhost:8086/api/accounts/account/${exchangeProperty.id}") // send to service
                .to("jms:queue:http-putAccount-response");  // HTTP response ends up in this queue
    }

}
