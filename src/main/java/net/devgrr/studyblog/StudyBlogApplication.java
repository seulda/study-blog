package net.devgrr.studyblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StudyBlogApplication {

  public static void main(String[] args) {
    SpringApplication.run(StudyBlogApplication.class, args);
  }
}
