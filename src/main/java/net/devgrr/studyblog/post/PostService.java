package net.devgrr.studyblog.post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.devgrr.studyblog.config.exception.BaseException;
import net.devgrr.studyblog.config.exception.ErrorCode;
import net.devgrr.studyblog.config.mapStruct.PostMapper;
import net.devgrr.studyblog.member.MemberService;
import net.devgrr.studyblog.member.entity.Member;
import net.devgrr.studyblog.post.dto.PostRequest;
import net.devgrr.studyblog.post.entity.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostService {

  private final PostRepository postRepository;
  private final MemberService memberService;
  private final PostMapper postMapper;
  @PersistenceContext private EntityManager entityManager;

  public List<Post> getPosts() {
    return postRepository.findAllByIsDraftFalse();
  }

  public Post existPostsById(Integer id) throws BaseException {
    return postRepository
        .findById(id)
        .orElseThrow(
            () ->
                new BaseException(ErrorCode.INVALID_INPUT_VALUE, "Post not found with id: " + id));
  }

  @Transactional
  public Post getPostsById(Integer id, String userId) throws BaseException {
    Post post = existPostsById(id);
    if (!post.getWriter().getUserId().equals(userId)) {
      postRepository.incrementViewCount(id);
      entityManager.refresh(post);
    }
    return post;
  }

  public List<Post> getPostsByKeywords(String title, String content, String tag) {
    return postRepository.findAllByTitleContainingOrContentContainingOrTagContaining(
        title, content, tag);
  }

  @Transactional
  public Post setPosts(PostRequest req, String userId) {
    Member member = memberService.selectUserByUserId(userId);
    Post post = postMapper.toPost(req, member);
    postRepository.save(post);
    return post;
  }

  @Transactional
  public void putPostsById(Integer id, PostRequest req, String userId) throws BaseException {
    Post post = existPostsById(id);
    if (!post.getWriter().getUserId().equals(userId)) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "수정 권한이 없습니다.");
    }
    Post updPost = postMapper.putPostMapper(req, post);
    postRepository.save(updPost);
  }

  @Transactional
  public void delPostsById(Integer id, String userId) throws BaseException {
    Post post = existPostsById(id);
    if (!post.getWriter().getUserId().equals(userId)) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "삭제 권한이 없습니다.");
    }
    postRepository.delete(post);
  }

  @Transactional
  public void likePostsById(Integer id, String userId) throws BaseException {
    Post post = existPostsById(id);

    if (post.getWriter().getUserId().equals(userId)) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "본인 게시글은 추천할 수 없습니다.");
    }

    if (post.getLikes().stream().anyMatch(member -> member.getUserId().equals(userId))) {
      // unlike
      post.getLikes().removeIf(member -> member.getUserId().equals(userId));
      postRepository.save(post);
    } else {
      // like
      post.getLikes().add(memberService.selectUserByUserId(userId));
      postRepository.save(post);
    }
  }
}
