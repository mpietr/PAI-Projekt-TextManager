package put.poznan.textmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import put.poznan.textmanager.model.TextResource;
import put.poznan.textmanager.model.User;

import java.util.List;
import java.util.Optional;

public interface TextResourceRepository extends JpaRepository<TextResource, String> {
    List<TextResource> findAllByOwnerId(User user);

    List<TextResource> findAllByNameContainingAndOwnerId(String name, User ownerId);
    List<TextResource> findAllByTextContainingAndOwnerId(String text, User ownerId);

    List<TextResource> findAllByNameContainingAndTextContainingAndOwnerId(String name, String text, User ownerId);

    List<TextResource> findAllByTagsContainingAndOwnerId(String tags, User ownerId);
    void deleteTextResourceByCode(String code);

    Optional<TextResource> findTextResourceByCode(String code);

    Optional<TextResource> findTextResourceByNameAndOwnerId(String name, User ownerId);
}
