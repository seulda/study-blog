package net.devgrr.interp.log.api.config.mapStruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemberMapperConfig {
  @Bean
  public MemberMapper memberMapper() {
    return new MemberMapperImpl();
  }
}
