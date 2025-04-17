package br.com.minirs.exceptions.user;

public class UserDisabledException extends RuntimeException {
    public UserDisabledException(){
        super("User disabled");
    }
}
