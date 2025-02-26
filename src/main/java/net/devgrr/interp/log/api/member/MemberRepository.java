package net.devgrr.interp.log.api.member;

import java.util.Optional;
import net.devgrr.interp.log.api.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
  Optional<Member> findByUserId(String userId);

  boolean existsByUserId(String userId);

  boolean existsByEmail(String email);

  Optional<Member> findByRefreshToken(String refreshToken);

  void deleteByUserId(String userId);
}
