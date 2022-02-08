package com.cos.insta.repository;

import com.cos.insta.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    List<User> findAllByUsernameOrName(String username, String name);
}
