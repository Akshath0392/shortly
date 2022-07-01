package com.akshath.shortly.repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akshath.shortly.connectors.HashKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Shortly {
    
    private String originalurl;
    private String shorturl;
    private String hash;
    private Date createdon;
    private Date expireson;
    private Integer expired;
    private Long accesscount;
    private Date deletedon;
    private Integer deleted;

    @Value("${app.shortly.url}")
    String shortlyurl;

    @Autowired
    ShortlyRepository repository;

    @Autowired
    HashKey connector;

    public String create(String originalurl) {
        String shorturl = "";

        String hash = connector.generateHash(originalurl);
        if(hash.isEmpty()) {
            return shorturl;
        }

        shorturl = shortlyurl + "/" + hash;

        long now = new Date().getTime();
        Timestamp timestamp = new  Timestamp(now);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("originalurl", originalurl);
        data.put("shorturl", shorturl);
        data.put("hash", hash);
        data.put("createdon", timestamp);

        repository.insert("ShortUrl", data);
        return shorturl;
    }

    public String getOriginalURL(String id) {
        String originalurl = "";
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("hash", id);

        List<Map<String, Object>> rows = repository.select("ShortUrl", criteria);
        if(rows.size() > 0) {
            Map<String, Object> row = rows.get(0);
            originalurl = row.get("originalurl").toString();
        }

        return originalurl;
    }

}
