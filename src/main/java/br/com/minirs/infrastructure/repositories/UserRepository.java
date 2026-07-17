package br.com.minirs.infrastructure.repositories;

import br.com.minirs.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByUserName(String userName);

    @Query("SELECT u FROM User u WHERE u.active = true")
    List<User> findActiveUsers();
}
