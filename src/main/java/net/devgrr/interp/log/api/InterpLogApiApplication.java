package net.devgrr.interp.log.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class InterpLogApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(InterpLogApiApplication.class, args);
  }
}
