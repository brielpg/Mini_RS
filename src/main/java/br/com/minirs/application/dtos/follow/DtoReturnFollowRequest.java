package br.com.minirs.application.dtos.follow;

import br.com.minirs.application.dtos.user.DtoReturnUser;
import br.com.minirs.domain.entities.FollowRequest;

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
