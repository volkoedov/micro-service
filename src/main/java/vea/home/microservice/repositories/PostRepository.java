package vea.home.microservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vea.home.microservice.entities.Post;

import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post,Long> {
    Set<Post> findByUserId(Long serId);

    Optional<Post> findByIdAndUserId(Long id, Long userId);
}
