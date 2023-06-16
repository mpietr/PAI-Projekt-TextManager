package put.poznan.textmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import put.poznan.textmanager.exception.UserNotFoundException;
import put.poznan.textmanager.model.User;
import put.poznan.textmanager.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository.findUserByUserId(id)
                .orElseThrow(() -> new UserNotFoundException("User by id: " + id + "not found"));
    }

    public User findUserByUsername(String name) {
        return userRepository.findUserByUsername(name)
                .orElseThrow(() -> new UserNotFoundException("User by name: " + name + "not found"));
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User by name: " + email + "not found"));
    }

    public User findUserByCode(String code) {
        return userRepository.findUserByResetUrl(code)
                .orElseThrow(() -> new UserNotFoundException("User by name: " + code + "not found"));
    }

}
