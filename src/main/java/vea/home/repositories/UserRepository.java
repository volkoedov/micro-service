package vea.home.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vea.home.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}