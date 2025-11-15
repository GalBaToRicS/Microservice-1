package ru.itmentor.spring.boot_security.demo.repository;

import ru.itmentor.spring.boot_security.demo.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    boolean existsUserByLogin(String login);
}