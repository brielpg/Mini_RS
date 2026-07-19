package br.com.minirs.domain.exceptions;

public class ResourceDisabledException extends RuntimeException{
    public ResourceDisabledException(String message){
        super(message);
    }
}
