package net.devgrr.studyblog.comment;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.devgrr.studyblog.comment.dto.CommentRequest;
import net.devgrr.studyblog.comment.dto.CommentResponse;
import net.devgrr.studyblog.comment.entity.Comment;
import net.devgrr.studyblog.config.exception.BaseException;
import net.devgrr.studyblog.config.exception.ErrorCode;
import net.devgrr.studyblog.config.mapStruct.CommentMapper;
import net.devgrr.studyblog.member.MemberService;
import net.devgrr.studyblog.member.entity.Member;
import net.devgrr.studyblog.post.PostService;
import net.devgrr.studyblog.post.entity.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

  private final CommentRepository commentRepository;
  private final CommentMapper commentMapper;

  private final PostService postService;
  private final MemberService memberService;

  public List<Comment> getCommentsByPostId(Integer postId) {
    return commentRepository.findAllByPostId(postId);
  }

  public List<CommentResponse> getCommentsByPostIdNested(Integer postId) {
    List<Comment> comments = getCommentsByPostId(postId);
    return comments.stream()
        .filter(comment -> comment.getParentCommentId() == null)
        .map(
            rootComment -> {
              List<CommentResponse> childComments =
                  comments.stream()
                      .filter(
                          comment ->
                              Objects.equals(comment.getParentCommentId(), rootComment.getId()))
                      .map(commentMapper::toResponse)
                      .collect(Collectors.toList());
              return commentMapper.toResponseWithChildren(rootComment, childComments);
            })
        .collect(Collectors.toList());
  }

  public Comment existCommentById(Integer id) throws BaseException {
    return commentRepository
        .findById(id)
        .orElseThrow(
            () ->
                new BaseException(
                    ErrorCode.INVALID_INPUT_VALUE, "Comment not found with id: " + id));
  }

  @Transactional
  public Comment setComments(CommentRequest req, String userId) throws BaseException {
    Member member = memberService.selectUserByUserId(userId);
    Post post = postService.existPostsById(req.postId());
    Comment comment = commentMapper.toComment(req, post, member);
    commentRepository.save(comment);
    return comment;
  }

  @Transactional
  public void putCommentsById(CommentRequest req, String userId) throws BaseException {
    Comment comment = existCommentById(req.id());
    if (!comment.getWriter().getUserId().equals(userId)) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "수정 권한이 없습니다.");
    }
    Comment updComment = commentMapper.putCommentMapper(req, comment);
    commentRepository.save(updComment);
  }

  @Transactional
  public void delCommentsById(Integer id, String userId) throws BaseException {
    Comment comment = existCommentById(id);
    if (!comment.getWriter().getUserId().equals(userId)) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "삭제 권한이 없습니다.");
    }
    commentRepository.delete(comment);
  }

  @Transactional
  public void likeCommentsById(Integer id, String userId) throws BaseException {
    Comment comment = existCommentById(id);

    if (comment.getWriter().getUserId().equals(userId)) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "본인 댓글에는 추천할 수 없습니다.");
    }

    if (comment.getLikes().stream().anyMatch(member -> member.getUserId().equals(userId))) {
      // unlike
      comment.getLikes().removeIf(member -> member.getUserId().equals(userId));
    } else {
      // like
      comment.getLikes().add(memberService.selectUserByUserId(userId));
    }
    commentRepository.save(comment);
  }
}
