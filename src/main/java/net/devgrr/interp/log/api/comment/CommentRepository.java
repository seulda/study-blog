package net.devgrr.interp.log.api.comment;

import java.util.List;
import net.devgrr.interp.log.api.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

  List<Comment> findAllByPostId(Integer postId);
}
