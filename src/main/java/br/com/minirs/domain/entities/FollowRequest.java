package br.com.minirs.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "minirs_follow_requests")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode(of = "id")
public class FollowRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate requestDate;
    private Boolean accepted;
    private Boolean active;
    @ManyToOne
    @JoinColumn(name = "requesterId")
    private User requester;
    @ManyToOne
    @JoinColumn(name = "requestedId")
    private User requested;

    public FollowRequest(User requester, User requested){
        this.requestDate = LocalDate.now();
        this.requester = requester;
        this.requested = requested;
        this.accepted = false;
        this.active = true;
    }

    public void acceptRequest() {
        this.accepted = true;
        this.active = false;
        this.requester.followUser(this.requested);
    }

    public void denyRequest() {
        this.accepted = false;
        this.active = false;
    }
}
