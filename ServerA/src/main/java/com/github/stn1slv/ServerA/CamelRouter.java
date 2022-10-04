package com.github.stn1slv.ServerA;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;

@Component
public class CamelRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("netty-http:{{serviceEndpoint}}").streamCaching()
                .log(LoggingLevel.INFO, "New request: ${body}")
                .doTry()
                    .to("json-validator:{{schemaLocation}}")
                    .setHeader(HTTP_RESPONSE_CODE, constant("200"))
                    .setBody(simple(""))
                .doCatch(Exception.class)
                    .setHeader(HTTP_RESPONSE_CODE, constant("422"))
//                    .to("log:debug?showAll=true&multiline=true")
                    .setBody(exchangeProperty(Exchange.EXCEPTION_CAUGHT))
                .end();
        ;
    }
}
