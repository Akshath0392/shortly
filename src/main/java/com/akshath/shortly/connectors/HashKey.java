package com.akshath.shortly.connectors;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;

public class HashKey {
    
    @Value("${app.shortly.hash.method}")
    String hashMethod;

    @Value("${app.shortly.hash.length}")
    Integer hashLength;

    Logger log = Logger.getLogger(this.getClass().getCanonicalName());

    public String generateHash(String url) {
        String hash = "";

        try {
            switch(hash) {
                //TODO: Enable support to use key-generation service.
                default:
                    hash = this.generateHashLocally(url);
            }
        } catch(Exception e) {
            log.severe(e.getMessage());
            log.severe(Arrays.toString(e.getStackTrace()));

            return hash;
        }

        return hash;
    }

    private String generateHashLocally(String url) throws NoSuchAlgorithmException{
        String randomString = UUID.randomUUID().toString().replace("-", "");
        Long l = ByteBuffer.wrap(randomString.getBytes()).getLong();
        return Long.toString(l, hashLength);
    }

}
