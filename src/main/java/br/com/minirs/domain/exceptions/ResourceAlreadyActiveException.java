package br.com.minirs.domain.exceptions;

public class ResourceAlreadyActiveException extends RuntimeException {
    public ResourceAlreadyActiveException(String message){
        super(message);
    }
}
