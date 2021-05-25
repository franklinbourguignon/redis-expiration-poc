package com.expiration.redisexpirationpoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class RedisExpirationPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisExpirationPocApplication.class, args);
	}

}
