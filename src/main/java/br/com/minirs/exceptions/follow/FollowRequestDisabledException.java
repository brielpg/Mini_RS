package br.com.minirs.exceptions.follow;

public class FollowRequestDisabledException extends RuntimeException {
    public FollowRequestDisabledException(String message) {
        super(message);
    }

    public FollowRequestDisabledException(){
        super("Follow Request is not active");
    }
}
