package com.hbb.springrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbb.springrest.config.TestRestTemplateConfiguration;
import com.hbb.springrest.dto.PersonRequestDTO;
import com.hbb.springrest.dto.PersonResponseDTO;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestRestTemplateConfiguration.class)
class PersonControllerTest {

    private static final String age = "20";
    private static final String name = "John";
    private String payload;
    private ResponseEntity responseEntity;
    private String responseBodyDecrypted;
    private PersonResponseDTO personResponseDTO;

    @Autowired
    TestRestTemplate testRestTemplate;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void testBase64Payload() throws Exception {
        givenBase64ValidPayload();
        whenRequestIsSent();
        thenResponseIs200();
        thenResponseBodyIsBas64Encrypted();
        thenDecryptResponseBody();
        thenBase64DecodedResponseHasValidAge();
    }

    private void thenBase64DecodedResponseHasValidAge() {
        assertEquals("ages must match", age, personResponseDTO.getAge());
    }

    private void thenDecryptResponseBody() throws Exception {
        responseBodyDecrypted = new String(Base64.decodeBase64((String) responseEntity.getBody()));
        personResponseDTO = new ObjectMapper().readValue(responseBodyDecrypted, PersonResponseDTO.class);
    }

    private void thenResponseBodyIsBas64Encrypted() {
        String responseBody = (String) responseEntity.getBody();
        assertTrue("Response body must be base64 encoded", Base64.isBase64(responseBody));
    }

    private void thenResponseIs200() {
        assertEquals("Expected 200 response code", HttpStatus.OK, responseEntity.getStatusCode());
    }

    private void whenRequestIsSent() {
        URI uri = URI.create("http://localhost:" + randomServerPort + "/person");
        RequestEntity requestEntity = RequestEntity.post(uri).contentType(MediaType.TEXT_PLAIN).body(payload);
        responseEntity = testRestTemplate.exchange(requestEntity, String.class);
    }

    private void givenBase64ValidPayload() throws Exception {
        PersonRequestDTO personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setAge(age);
        personRequestDTO.setName(name);
        String rawPayload = new ObjectMapper().writeValueAsString(personRequestDTO);
        payload = Base64.encodeBase64String(rawPayload.getBytes(StandardCharsets.UTF_8));
    }


}