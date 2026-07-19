package br.com.minirs.domain.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_follows", uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "followed_id"}))
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_id", nullable = false)
    private User followed;

    @Column(name = "close_friend", nullable = false)
    private boolean closeFriend = false;

    @Column(name = "followed_at", nullable = false, updatable = false)
    private LocalDateTime followedAt = LocalDateTime.now();

    protected Follow() {
    }

    public Follow(User follower, User followed) {
        if (follower == null)
            throw new IllegalArgumentException("Follower must not be null");

        if (followed == null)
            throw new IllegalArgumentException("Followed must not be null");

        if (follower.equals(followed))
            throw new IllegalArgumentException("Cannot follow yourself");

        this.follower = follower;
        this.followed = followed;
    }

    public Long getId() {
        return id;
    }

    public User getFollower() {
        return follower;
    }

    public User getFollowed() {
        return followed;
    }

    public boolean isCloseFriend() {
        return closeFriend;
    }

    public void addToCloseFriend() {
        if (closeFriend) {
            throw new IllegalStateException("User is already in close friends.");
        }

        this.closeFriend = true;
    }

    public void removeFromCloseFriend() {
        if (!closeFriend) {
            throw new IllegalStateException("User is not in close friends.");
        }

        this.closeFriend = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Follow follow)) return false;
        return id != null && Objects.equals(id, follow.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
