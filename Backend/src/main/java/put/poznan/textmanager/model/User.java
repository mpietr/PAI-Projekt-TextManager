package put.poznan.textmanager.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import org.mindrot.jbcrypt.BCrypt;

@Entity
public class User implements Serializable {
    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(name = "username", nullable = false, updatable = false)
    private String username;
    @Column(unique = true)
    private String email;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String password;

    private String resetUrl;

    public User(){}

    public User(Long userId, String username, String email, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResetUrl() {
        return resetUrl;
    }

    public void setResetUrl(String resetUrl) {
        this.resetUrl = resetUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHashedPassword(String pass) {
        String salt = BCrypt.gensalt(10);
        this.password = BCrypt.hashpw(pass, salt);;
    }


    public boolean checkPassword(String pass) {
        return BCrypt.checkpw(pass, this.password);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
