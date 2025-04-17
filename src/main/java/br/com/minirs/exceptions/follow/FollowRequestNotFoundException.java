package br.com.minirs.exceptions.follow;

public class FollowRequestNotFoundException extends RuntimeException {
    public FollowRequestNotFoundException(Long id) {
        super("Follow Request with ID "+id+" not found");
    }

    public FollowRequestNotFoundException(){
        super("Follow Request not found");
    }
}
