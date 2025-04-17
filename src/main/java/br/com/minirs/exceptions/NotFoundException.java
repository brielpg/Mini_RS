package br.com.minirs.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(){
        super("Resource not found");
    }

    public NotFoundException(String message){
        super(message);
    }

    public NotFoundException(Long id){
        super("Resource with ID " + id + " not found");
    }

    public NotFoundException(String resource, Long id){
        super(resource + " with ID " + id + " not found");
    }
}
