package br.com.minirs.exceptions.follow;

public class InvalidFollowRequestException extends RuntimeException {
    public InvalidFollowRequestException(String message) {
        super(message);
    }
}
