package net.devgrr.studyblog.post.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import net.devgrr.studyblog.member.entity.Member;
import net.devgrr.studyblog.model.entity.BaseEntity;

@Getter
@Setter
@Builder
@Entity
@Table(name = "post")
@Schema(description = "게시글 엔티티")
@AllArgsConstructor
public class Post extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "게시글 ID")
  private Integer id;

  @Column(nullable = false)
  @Schema(description = "게시글 제목")
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  @Schema(description = "게시글 내용")
  private String content;

  @Column(nullable = false)
  @Schema(description = "임시저장 여부 (true: 임시저장, false: 저장완료)")
  private Boolean isDraft;

  @ManyToOne
  @JoinColumn(name = "member_id", nullable = false)
  @Schema(description = "작성자")
  private Member writer;

  @ManyToMany
  @Schema(description = "추천")
  private Set<Member> likes;

  @Schema(description = "태그")
  private String tag;

  @Schema(description = "조회수")
  @Default
  private Integer viewCount = 0;

  public Post() {}
}
