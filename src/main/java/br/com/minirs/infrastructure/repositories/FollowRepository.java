package br.com.minirs.infrastructure.repositories;

import br.com.minirs.domain.entities.Follow;
import br.com.minirs.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(User follower);
    List<Follow> findByFollowed(User followed);
    Optional<Follow> findByFollowerAndFollowed(User follower, User followed);
}
