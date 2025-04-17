package br.com.minirs.exceptions.user;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException(){
        super("Email already registered");
    }

    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
