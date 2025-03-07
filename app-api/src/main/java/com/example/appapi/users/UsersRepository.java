package com.example.appapi.users;

import com.example.appapi.users.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUserId(String username);

    boolean existsByEmail(String email);
    boolean existsByUserId(String userId);
}
