package com.akshath.shortly.controllers;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

import com.akshath.shortly.services.ShortlyService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class ShortlyController {
    
    Logger log = Logger.getLogger(this.getClass().getCanonicalName());

    @Autowired
    ShortlyService service;

    @PostMapping("/api/shortly/create")
    public @ResponseBody ResponseEntity<Map<String, Object>> shortner(@RequestParam(name="url") String url) {
        JSONObject response = new JSONObject();

        log.info("Original URL"+ url);

        if(url.isEmpty()) {
            response.put("success", false);
            response.put("error", "URL is empty"); 
            return new ResponseEntity<>(response.toMap(), HttpStatus.OK);
        }

        try{
            log.info("Short URL geneation ... STARTED");
            String shorturl = service.shortenURL(url);

            if(shorturl.isEmpty()) {
                log.info("Short URL geneation ... FAILED");    

                response.put("success", false);
                response.put("error", "Internal server error"); 
            } else {
                log.info("Short URL geneation ... COMPLETED");
                log.info("Short URL" + shorturl);

                JSONObject result = new JSONObject();
                result.put("url", url);
                result.put("shorturl", shorturl);
                
                response.put("success", true);
                response.put("result", result);
            }
        } catch(Exception e) {
            log.severe(e.getMessage());
            log.severe(Arrays.toString(e.getStackTrace()));
            response.put("success", false);
            response.put("error", "Internal server error");
        }

        return new ResponseEntity<>(response.toMap(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> redirect(@PathVariable("id") String id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        log.info("URL identifier ... ("+id+")");

        try {
            String redirectURL = service.getRedirectionURL(id);

            if(redirectURL.isEmpty()) {
                log.info("Redirection URL ... NOT FOUND");
                return new ResponseEntity<>(httpHeaders, HttpStatus.NOT_FOUND);
            } else {
                log.info("Redirection URL ... FOUND");

                URI uri = new URI(redirectURL);
                httpHeaders.setLocation(uri);

                log.info("Redirecting   ... " + redirectURL);
            }
        } catch (Exception e) {
            log.severe(e.getMessage());
            log.severe(Arrays.toString(e.getStackTrace()));

            return new ResponseEntity<>(httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
}
