package br.com.minirs.domain.exceptions.user;

public class UserNameAlreadyRegisteredException extends RuntimeException {
    public UserNameAlreadyRegisteredException(){
        super("There is already a user with this username");
    }
}
