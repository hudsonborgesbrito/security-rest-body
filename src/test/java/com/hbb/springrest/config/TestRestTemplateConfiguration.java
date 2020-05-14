package com.hbb.springrest.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestRestTemplateConfiguration {

    @Bean
    public TestRestTemplate testRestTemplate(){
        return new TestRestTemplate();
    }
}
