package com.example.teamtwelvebackend.user.repository;

import com.example.teamtwelvebackend.user.domain.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
