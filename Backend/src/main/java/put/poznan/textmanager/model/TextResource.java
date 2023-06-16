package put.poznan.textmanager.model;

import jakarta.persistence.*;
import org.hibernate.engine.jdbc.env.internal.LobTypes;

import java.io.Serializable;

@Entity
@Table(name = "TextResource")
public class TextResource implements Serializable {

    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User ownerId;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String text;
    private String tags;

    @Id
    @Column(nullable = false, updatable = false)
    private String code;


    public TextResource(){}

    public TextResource(String name, User ownerId, String text, String tags, String code) {
        this.name = name;
        this.ownerId = ownerId;
        this.text = text;
        this.tags = tags;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(User ownerId) {
        this.ownerId = ownerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "TextResource{" +
                "name='" + name + '\'' +
                ", ownerId=" + ownerId +
                ", text='" + text + '\'' +
                ", tags='" + tags + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

}
