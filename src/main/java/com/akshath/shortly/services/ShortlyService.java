package com.akshath.shortly.services;

import java.util.logging.Logger;

import com.akshath.shortly.repository.Shortly;

import org.springframework.beans.factory.annotation.Autowired;

public class ShortlyService {
    
    Logger log = Logger.getLogger(this.getClass().getCanonicalName());

    @Autowired
    Shortly model;

    public String shortenURL(String url) {
        String shorturl  = "";
        shorturl = model.create(url);
        return shorturl;
    }

    public String getRedirectionURL(String id) {
        return model.getOriginalURL(id);
    }
}
