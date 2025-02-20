package net.devgrr.studyblog.post;

import java.util.List;
import net.devgrr.studyblog.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Integer> {
  List<Post> findAllByTitleContainingOrContentContainingOrTagContaining(
      String title, String content, String tag);

  List<Post> findAllByIsDraftFalse();

  @Modifying
  @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :id")
  void incrementViewCount(@Param("id") Integer id);
}
