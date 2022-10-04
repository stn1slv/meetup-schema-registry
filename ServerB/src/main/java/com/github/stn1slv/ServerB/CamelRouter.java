package com.github.stn1slv.ServerB;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class CamelRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("kafka:{{kafka.inputTopic}}?brokers={{camel.component.kafka.brokers}}").streamCaching()
                .log(LoggingLevel.INFO, "New request: ${body}")
                .doTry()
                    .to("validator:{{schemaLocation}}")
                    /**
                     * if message valid then send it to output topic
                     */
                    .to("kafka:{{kafka.outputTopic}}?brokers={{camel.component.kafka.brokers}}")
                .doCatch(Exception.class)
                    .log(LoggingLevel.ERROR, "${exception.message}")
                    /**
                     * add error details for message in dql
                     */
                    .setHeader("error_msg",simple("${exception.message}"))
                    .to("kafka:{{kafka.dlqTopic}}?brokers={{camel.component.kafka.brokers}}")
                .end();
    }
}
