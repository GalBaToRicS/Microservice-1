package ru.itmentor.spring.boot_security.demo.repository;

import ru.itmentor.spring.boot_security.demo.constant.RoleName;
import ru.itmentor.spring.boot_security.demo.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}