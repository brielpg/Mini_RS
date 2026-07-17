package br.com.minirs.domain.exceptions.follow;

public class InvalidFollowRequestException extends RuntimeException {
    public InvalidFollowRequestException(String message) {
        super(message);
    }
}
