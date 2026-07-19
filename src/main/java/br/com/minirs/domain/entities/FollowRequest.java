package br.com.minirs.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_follow_requests")
public class FollowRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_id", nullable = false)
    private User requested;

    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt = LocalDateTime.now();

    protected FollowRequest() {
    }

    public FollowRequest(User requester, User requested){
        if (requester == null)
            throw new IllegalArgumentException("Requester must not be null");

        if (requested == null)
            throw new IllegalArgumentException("Requested must not be null");

        if (requester.equals(requested))
            throw new IllegalArgumentException("Cannot request follow to yourself");

        this.requested = requested;
        this.requester = requester;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public User getRequester() {
        return requester;
    }

    public User getRequested() {
        return requested;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FollowRequest that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
