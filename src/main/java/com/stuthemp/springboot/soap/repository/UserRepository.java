package com.stuthemp.springboot.soap.repository;

import com.stuthemp.springboot.soap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByLogin(String login);

    boolean deleteUserByLogin(String login);
}
