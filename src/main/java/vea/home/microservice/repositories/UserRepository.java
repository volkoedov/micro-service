package vea.home.microservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vea.home.microservice.entities.User;


public interface UserRepository extends JpaRepository<User, Long> {
}