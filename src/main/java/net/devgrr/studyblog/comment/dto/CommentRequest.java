package net.devgrr.studyblog.comment.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "댓글 요청")
public record CommentRequest(
    @Schema(description = "게시글 ID")
        @NotNull(message = "필수값: 게시글 id", groups = CommentValidationGroups.setGroup.class)
        @JsonView(CommentValidationGroups.setGroup.class)
        Integer postId,
    @Schema(description = "댓글 ID")
        @NotNull(
            message = "필수값: id",
            groups = {
              CommentValidationGroups.putGroup.class,
              CommentValidationGroups.idGroup.class
            })
        @JsonView({CommentValidationGroups.putGroup.class, CommentValidationGroups.idGroup.class})
        Integer id,
    @Schema(description = "부모 댓글 ID") @JsonView(CommentValidationGroups.setGroup.class)
        Integer parentCommentId,
    @Schema(description = "댓글 내용")
        @NotBlank(
            message = "필수값: content",
            groups = {
              CommentValidationGroups.setGroup.class,
              CommentValidationGroups.putGroup.class
            })
        @JsonView({CommentValidationGroups.setGroup.class, CommentValidationGroups.putGroup.class})
        String content) {}
