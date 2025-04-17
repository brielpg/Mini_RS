package br.com.minirs.exceptions.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(){
        super("User not found");
    }

    public UserNotFoundException(Long id) {
        super("User with ID "+id+" not found");
    }
}
