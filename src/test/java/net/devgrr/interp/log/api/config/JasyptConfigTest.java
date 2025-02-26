package net.devgrr.interp.log.api.config;

import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

public class JasyptConfigTest {

  @Test
  void jasypt() {
    String url = "jdbc:h2:../test";
    String username = "sa";
    String password = "";

    String encryptUrl = jasyptEncrypt(url);
    String encryptUsername = jasyptEncrypt(username);
    String encryptPassword = jasyptEncrypt(password);

    System.out.println("encryptUrl : " + encryptUrl);
    System.out.println("encryptUsername : " + encryptUsername);
    System.out.println("encryptPassword : " + encryptPassword);

    Assertions.assertThat(url).isEqualTo(jasyptDecryt(encryptUrl));
  }

  private String jasyptEncrypt(String input) {
    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    encryptor.setAlgorithm("PBEWithMD5AndDES");
    encryptor.setPassword("devgrr");
    return encryptor.encrypt(input);
  }

  private String jasyptDecryt(String input) {
    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    encryptor.setAlgorithm("PBEWithMD5AndDES");
    encryptor.setPassword("devgrr");
    return encryptor.decrypt(input);
  }
}
