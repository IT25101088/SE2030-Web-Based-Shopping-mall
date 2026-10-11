package com.sliit.se2030.mall.user.repository;

import com.sliit.se2030.mall.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Extending JpaRepository<User, Long> gives us save(), findById(), findAll(),
 * delete() etc. for free -- Spring generates the implementation at startup,
 * we never write one.
 *
 * findByEmail / existsByEmail below have NO method body: Spring Data parses
 * the method *name* itself ("find by Email") and builds the query from it.
 * This is the login lookup -- since User uses single-table inheritance, this
 * one query can return a Customer, Merchant, or PlatformEmployee.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
