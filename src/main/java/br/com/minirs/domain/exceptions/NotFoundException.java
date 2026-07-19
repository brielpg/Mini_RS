package br.com.minirs.domain.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String resource, Long id){
        super(resource + " with ID " + id + " not found");
    }
}
