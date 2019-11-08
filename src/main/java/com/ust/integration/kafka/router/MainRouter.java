package com.ust.integration.kafka.router;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MainRouter extends RouteBuilder {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Override
    public void configure() throws Exception {

      
        from("timer://producer?period=1000")
                .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        String message = UUID.randomUUID().toString();
                        producerTemplate.sendBody("kafka:{{kafka.topic}}?brokers={{kafka.server}}:{{kafka.port}}", message);
                    }
                });
        
        from("kafka:{{kafka.topic}}?brokers={{kafka.server}}:{{kafka.port}}&groupId={{kafka.channel}}")
        .process(new Processor() {
            public void process(Exchange exchange) throws Exception {
                System.out.println("Message Body : " + exchange.getIn().getBody());
            }
        });


    }
}
