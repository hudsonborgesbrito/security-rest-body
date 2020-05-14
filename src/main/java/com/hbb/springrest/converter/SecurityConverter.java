package com.hbb.springrest.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.util.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class SecurityConverter extends AbstractHttpMessageConverter<Object> {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConverter.class);

    @Autowired
    ObjectMapper objectMapper;

    public SecurityConverter() {
        super(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN);
    }

    @Override
    protected boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    protected Object readInternal(Class<?> aClass, HttpInputMessage httpInputMessage) throws IOException {
        return objectMapper.readValue(decrypt(httpInputMessage.getBody()), aClass);
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage httpOutputMessage) throws IOException {
        httpOutputMessage.getBody().write(encrypt(objectMapper.writeValueAsBytes(o)));
    }

    private String decrypt(InputStream inputStream) {
        // do your decryption here
        String rawInput = IOUtils.toString(inputStream);
        //Base64.isArrayByteBase64(
        logger.info("decrypt httpInputMessage: {}", rawInput);
        if (Base64.isBase64(rawInput)) {
            String decodedText = new String(Base64.decodeBase64(rawInput));
            logger.info("decrypted httpInputMessage: {}", decodedText);
            return decodedText;
        }
        return rawInput;
    }

    private byte[] encrypt(byte[] bytesToEncrypt) {
        String rawOutput = new String(bytesToEncrypt, StandardCharsets.UTF_8);
        logger.info("encrypt: {}", rawOutput);
        byte[] encrypted = Base64.encodeBase64(bytesToEncrypt);
        logger.info("encrypted: {}", new String(encrypted, StandardCharsets.UTF_8));
        return encrypted;
    }
}
