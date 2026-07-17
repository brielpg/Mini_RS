package br.com.minirs.application.dtos.follow;

import br.com.minirs.application.dtos.user.UserResponse;
import br.com.minirs.domain.entities.FollowRequest;

import java.time.LocalDate;

public class FollowRequestResponse {
    public Long id;
    public LocalDate requestDate;
    public Boolean accepted;
    public Boolean active;
    public UserResponse requester;
    public UserResponse requested;

    public FollowRequestResponse(FollowRequest followRequest){
        this.id = followRequest.getId();
        this.requestDate = followRequest.getRequestDate();
        this.accepted = followRequest.getAccepted();
        this.active = followRequest.getActive();
        this.requester = new UserResponse(followRequest.getRequester());
        this.requested = new UserResponse(followRequest.getRequested());
    }
}
