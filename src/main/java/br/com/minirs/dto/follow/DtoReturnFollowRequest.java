package br.com.minirs.dto.follow;

import br.com.minirs.dto.user.DtoReturnUser;
import br.com.minirs.entities.FollowRequest;

import java.time.LocalDate;

public class DtoReturnFollowRequest {
    public Long id;
    public LocalDate requestDate;
    public Boolean accepted;
    public Boolean active;
    public DtoReturnUser requester;
    public DtoReturnUser requested;

    public DtoReturnFollowRequest(FollowRequest followRequest){
        this.id = followRequest.getId();
        this.requestDate = followRequest.getRequestDate();
        this.accepted = followRequest.getAccepted();
        this.active = followRequest.getActive();
        this.requester = new DtoReturnUser(followRequest.getRequester());
        this.requested = new DtoReturnUser(followRequest.getRequested());
    }
}
