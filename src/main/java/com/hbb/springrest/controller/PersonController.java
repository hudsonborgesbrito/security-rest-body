package com.hbb.springrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbb.springrest.dto.PersonRequestDTO;
import com.hbb.springrest.dto.PersonResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/person")
public class PersonController {
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @PostMapping
    public ResponseEntity<PersonResponseDTO> addPerson(@RequestBody PersonRequestDTO personRequestDTO) {
        logger.info("Entered addPerson");
        try {
            logger.info("spring processed requestBody: {}", new ObjectMapper().writeValueAsString(personRequestDTO));
        } catch (Exception e) {
            logger.error("Error ", e);
        }
        PersonResponseDTO personResponseDTO = new PersonResponseDTO();
        personResponseDTO.setUuid(UUID.randomUUID().toString());
        personResponseDTO.setAge(personRequestDTO.getAge());
        personResponseDTO.setName(personRequestDTO.getName());
        try {
            logger.info("spring processed response: {}", new ObjectMapper().writeValueAsString(personResponseDTO));
        } catch (Exception e) {
            logger.error("Error ", e);
        }
        return ResponseEntity.ok(personResponseDTO);
    }

}
