package org.januslabs.oauth2.jwt.mongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.januslabs.oauth2.jwt.base", "org.januslabs.oauth2.jwt.mongo"})
public class JwtMongoTestApplication {

  public static void main(String[] args) {
    SpringApplication.run(JwtMongoTestApplication.class, args);
  }
}
