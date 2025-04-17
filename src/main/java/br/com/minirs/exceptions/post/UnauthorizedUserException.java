package br.com.minirs.exceptions.post;

public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException(String message){
        super(message);
    }

    public UnauthorizedUserException(){
        super("User is not allowed to do it");
    }
}
