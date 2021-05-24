package com.expiration.redisexpirationpoc.redis.config;

import com.expiration.redisexpirationpoc.redis.subscriber.ExpiredQrCodeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfiguration {

    @Autowired
    private ExpiredQrCodeListener qrCodeListener;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        final RedisTemplate<String, String> template = new RedisTemplate<String, String>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer( new StringRedisSerializer() );
        template.setHashValueSerializer( new StringRedisSerializer() );
        template.setHashKeySerializer( new StringRedisSerializer() );
        template.setValueSerializer( new StringRedisSerializer() );
        template.setStringSerializer( new StringRedisSerializer() );
        return template;
    }

    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(qrCodeListener);
    }

    @Bean
    RedisMessageListenerContainer expiredKeyListenerContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener(), new PatternTopic("__keyevent@*__:expired"));
        return container;
    }

}
