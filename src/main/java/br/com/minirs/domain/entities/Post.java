package br.com.minirs.domain.entities;

import br.com.minirs.domain.exceptions.ResourceAlreadyActiveException;
import br.com.minirs.domain.exceptions.ResourceDisabledException;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "tb_posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User postOwner;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "published_at", nullable = false, updatable = false)
    private LocalDateTime publishedAt = LocalDateTime.now();

    @ElementCollection
    @CollectionTable(name = "post_likes", joinColumns = @JoinColumn(name = "post_id"))
    private Set<Long> likesByUserId = new HashSet<>();

    protected Post() {
    }

    public Post(String content, User postOwner) {
        if (postOwner == null) {
            throw new IllegalArgumentException("Post owner must not be null");
        }

        changeContent(content);
        this.postOwner = postOwner;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public User getPostOwner() {
        return postOwner;
    }

    public void changeContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content must not be null or blank");
        }
        if (content.length() > 1000) {
            throw new IllegalArgumentException("Content must be at most 1000 characters");
        }
        this.content = content.trim();
    }

    public void updatePost(String newContent) {
        changeContent(newContent);
    }

    public void delete() {
        if (!this.active) {
            throw new ResourceDisabledException("Post is already deactivated");
        }
        this.active = false;
    }

    public void reactivate() {
        if (this.active) {
            throw new ResourceAlreadyActiveException("Post is already active");
        }
        this.active = true;
    }

    public void likeBy(Long userId) {
        Objects.requireNonNull(userId, "User ID must not be null");
        if (likesByUserId.contains(userId)) {
            throw new IllegalStateException("User already liked this post");
        }
        this.likesByUserId.add(userId);
    }

    public void dislikeBy(Long userId) {
        Objects.requireNonNull(userId, "User ID must not be null");
        if (!this.likesByUserId.remove(userId)) {
            throw new IllegalStateException("User has not liked this post");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return id != null && Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
