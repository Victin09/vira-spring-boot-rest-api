package es.vira.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.vira.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(String username);

    User findByUsername(String username);

    @Transactional
    void deleteByUsername(String username);

}
