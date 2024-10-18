package com.foogaro.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foogaro.model.Beer;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class BeerService {

    private final JedisPooled jedis;
    private final ObjectMapper objectMapper;

    public BeerService(JedisPooled jedis, ObjectMapper objectMapper) {
        this.jedis = jedis;
        this.objectMapper = objectMapper;
    }

    public void createBeer(Beer beer) {
        saveBeer(beer);
    }

    public Beer readBeer(String name) {
        Object beerAsJson = jedis.jsonGet("beer:" + name);
        if (beerAsJson != null) {
            try {
                String json = objectMapper.writeValueAsString(beerAsJson);
                return objectMapper.readValue(json, Beer.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error during beer deserialization", e);
            }
        }
        return null;
    }

    public void updateBeer(String name, Beer beer) {
        saveBeer(new Beer(name, beer.abv(), beer.ibu()));
    }

    public void deleteBeer(String name) {
        jedis.jsonDel("beer:" + name);
    }

    private void saveBeer(Beer beer) {
        try {
            String beerAsJson = objectMapper.writeValueAsString(beer);
            jedis.jsonSet("beer:" + beer.name(), beerAsJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during beer serialization", e);
        }
    }
}