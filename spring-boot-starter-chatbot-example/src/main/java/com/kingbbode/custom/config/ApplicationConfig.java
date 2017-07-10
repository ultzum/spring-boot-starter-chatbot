package com.kingbbode.custom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * Created by YG on 2017-07-10.
 */
@Configuration
public class ApplicationConfig {
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName("127.0.0.1");
        jedisConnectionFactory.setPort(6379);
        jedisConnectionFactory.setTimeout(0);
        jedisConnectionFactory.setUsePool(true);
        
        return jedisConnectionFactory;
    }
}
