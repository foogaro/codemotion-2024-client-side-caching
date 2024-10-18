package com.foogaro.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.*;
import redis.clients.jedis.csc.CacheConfig;

import java.time.Duration;

@Configuration
public class RedisConfiguration {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${redis.host:localhost}") private String redis_host;
    @Value("${redis.host:6379}") private Integer redis_port;

    @Bean
    public JedisPooled jedisPooled(JedisClientConfig clientConfig, CacheConfig cacheConfig, GenericObjectPoolConfig poolConfig) {
        HostAndPort node = new HostAndPort(redis_host, redis_port);
        return new JedisPooled(node, clientConfig, cacheConfig, poolConfig);
    }

    @Bean
    public GenericObjectPoolConfig poolConfig() {
        GenericObjectPoolConfig<Connection> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(10); // Maximum number of connections
        poolConfig.setMaxIdle(5);   // Maximum number of idle connections
        poolConfig.setMinIdle(2);   // Minimum number of idle connections
        poolConfig.setTestWhileIdle(true); // Test connections while idle
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(10)); // Eviction policy timing
        poolConfig.setBlockWhenExhausted(true); // Block if connections exhausted
        poolConfig.setMaxWait(Duration.ofSeconds(2)); // Wait time when pool is exhausted
        return poolConfig;
    }

    @Bean
    public JedisClientConfig clientConfig() {
        return DefaultJedisClientConfig.builder()
                .resp3()
                .build();
    }

    @Bean
    public CacheConfig cacheConfig() {
        return CacheConfig.builder()
                .maxSize(1000) // Cache size
                .build();
    }
}