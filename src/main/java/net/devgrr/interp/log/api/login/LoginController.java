package net.devgrr.interp.log.api.login;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.devgrr.interp.log.api.member.dto.LoginRequest;
import net.devgrr.interp.log.api.member.dto.MemberValidationGroup;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@Tag(name = "LoginController", description = "로그인 API")
@RestController
public class LoginController {

  @Operation(description = "로그인을 한다.")
  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public void login(
      @Validated(MemberValidationGroup.loginGroup.class) @RequestBody LoginRequest req) {
    // JsonUsernamePasswordAuthenticationFilter 에서 처리
  }
}
