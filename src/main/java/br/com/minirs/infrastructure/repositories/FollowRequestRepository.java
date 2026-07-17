package br.com.minirs.infrastructure.repositories;

import br.com.minirs.domain.entities.FollowRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRequestRepository extends JpaRepository<FollowRequest, Long> {
    @Query("SELECT COUNT(fr) > 0 FROM FollowRequest fr WHERE fr.requester.id = :userRequester AND fr.requested.id = :userRequested AND fr.accepted = TRUE")
    boolean existsActiveFollowRequest(@Param("userRequester") Long userRequester, @Param("userRequested") Long userRequested);
}

