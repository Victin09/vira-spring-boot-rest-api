package es.vira.infraestructure.repository;

import es.vira.application.dto.UserDto;
import es.vira.domain.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<UserDto> findAllProjectionsBy(Pageable pageable);

    Optional<UserDto> findProjectionById(Long id);

    Optional<User> findByUsername(String username);

    Optional<UserDto> findProjectionByUsername(String username);

    boolean existsByUsername(String username);
}
