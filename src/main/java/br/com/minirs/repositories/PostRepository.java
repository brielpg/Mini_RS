package br.com.minirs.repositories;

import br.com.minirs.entities.Post;
import br.com.minirs.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByPostOwner(User user);
}
