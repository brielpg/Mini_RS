package br.com.minirs.exceptions.user;

public class UserNameAlreadyRegisteredException extends RuntimeException {
    public UserNameAlreadyRegisteredException(){
        super("There is already a user with this username");
    }

    public UserNameAlreadyRegisteredException(String message) {
        super(message);
    }
}
