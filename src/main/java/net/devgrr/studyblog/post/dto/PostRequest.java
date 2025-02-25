package net.devgrr.studyblog.post.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "게시글 요청")
public record PostRequest(
    @Schema(description = "게시글 ID")
        @NotNull(message = "필수값: id", groups = PostValidationGroups.idGroup.class)
        @JsonView(PostValidationGroups.idGroup.class)
        Integer id,
    @Schema(description = "게시글 제목")
        @NotBlank(message = "필수값: title", groups = PostValidationGroups.articleGroup.class)
        @JsonView(PostValidationGroups.articleGroup.class)
        String title,
    @Schema(description = "게시글 부제목") @JsonView(PostValidationGroups.articleGroup.class)
        String subTitle,
    @Schema(description = "게시글 내용")
        @NotBlank(message = "필수값: content", groups = PostValidationGroups.articleGroup.class)
        @JsonView(PostValidationGroups.articleGroup.class)
        String content,
    @Schema(description = "태그") @JsonView(PostValidationGroups.articleGroup.class) List<String> tag,
    @Schema(description = "임시저장 여부")
        @NotNull(message = "필수값: isDraft", groups = PostValidationGroups.articleGroup.class)
        @JsonView(PostValidationGroups.articleGroup.class)
        Boolean isDraft) {}
