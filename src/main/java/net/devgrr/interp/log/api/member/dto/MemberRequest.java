package net.devgrr.interp.log.api.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "회원 요청")
public record MemberRequest(
    @Schema(description = "회원 ID")
        @NotBlank(message = "필수값: userId", groups = MemberValidationGroup.createGroup.class)
        String userId,
    @Schema(description = "비밀번호")
        @NotBlank(message = "필수값: password", groups = MemberValidationGroup.createGroup.class)
        String password,
    @Schema(description = "이름")
        @NotBlank(message = "필수값: name", groups = MemberValidationGroup.createGroup.class)
        String name,
    @Schema(description = "이메일")
        @NotBlank(message = "필수값: email", groups = MemberValidationGroup.createGroup.class)
        @Email(message = "유효하지 않은 이메일 형식입니다.", groups = MemberValidationGroup.createGroup.class)
        String email,
    @Schema(description = "권한") String role) {}
