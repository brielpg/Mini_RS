package br.com.minirs.exceptions.user;

public class UserAlreadyActiveException extends RuntimeException{
    public UserAlreadyActiveException(String message){
        super(message);
    }
}
