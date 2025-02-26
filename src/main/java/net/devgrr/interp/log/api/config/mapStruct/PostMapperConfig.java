package net.devgrr.interp.log.api.config.mapStruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostMapperConfig {
  @Bean
  public PostMapper postMapper() {
    return new PostMapperImpl();
  }
}
