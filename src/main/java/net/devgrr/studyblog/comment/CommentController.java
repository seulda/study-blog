package net.devgrr.studyblog.comment;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.devgrr.studyblog.comment.dto.CommentRequest;
import net.devgrr.studyblog.comment.dto.CommentResponse;
import net.devgrr.studyblog.comment.dto.CommentValidationGroups;
import net.devgrr.studyblog.config.exception.BaseException;
import net.devgrr.studyblog.config.mapStruct.CommentMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "CommentController", description = "댓글 API")
@RestController
public class CommentController {

  private final CommentService commentService;
  private final CommentMapper commentMapper;

  @Operation(description = "댓글 목록을 조회한다.")
  @GetMapping("/{postId}")
  public List<CommentResponse> getCommentsByPost(
      @PathVariable("postId") @Parameter(description = "게시글 ID") Integer postId,
      @RequestParam(value = "nested", required = false, defaultValue = "false")
          @Parameter(description = "중첩구조 여부")
          Boolean nested) {
    return nested
        ? commentService.getCommentsByPostIdNested(postId)
        : commentService.getCommentsByPostId(postId).stream()
            .map(commentMapper::toResponse)
            .collect(Collectors.toList());
  }

  @Operation(description = "댓글을 생성한다.")
  @JsonView(CommentValidationGroups.setGroup.class)
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CommentResponse setComments(
      @Validated(CommentValidationGroups.setGroup.class) @RequestBody CommentRequest req,
      Principal principal)
      throws BaseException {
    return commentMapper.toResponse(commentService.setComments(req, principal.getName()));
  }

  @Operation(description = "댓글을 수정한다.")
  @JsonView(CommentValidationGroups.putGroup.class)
  @PutMapping
  public void putCommentsById(
      @Validated(CommentValidationGroups.putGroup.class) @RequestBody CommentRequest req,
      Principal principal)
      throws BaseException {
    commentService.putCommentsById(req, principal.getName());
  }

  @Operation(description = "댓글을 삭제한다.")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delCommentsById(
      @PathVariable("id") @Parameter(description = "댓글 ID") Integer id, Principal principal)
      throws BaseException {
    commentService.delCommentsById(id, principal.getName());
  }

  @Operation(description = "댓글을 추천 또는 취소한다.")
  @JsonView(CommentValidationGroups.idGroup.class)
  @PutMapping("/{id}/likes")
  public void likeCommentsById(
      @PathVariable("id") @Parameter(description = "댓글 ID") Integer id, Principal principal)
      throws BaseException {
    commentService.likeCommentsById(id, principal.getName());
  }
}
