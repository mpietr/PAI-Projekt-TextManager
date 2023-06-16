package put.poznan.textmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import put.poznan.textmanager.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findUserByUserId(Long id);

    Optional<User> findUserByUsername(String name);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByResetUrl(String resetUrl);
}
