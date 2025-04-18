package br.com.minirs.exceptions;

public class ResourceAlreadyActiveException extends RuntimeException {
    public ResourceAlreadyActiveException(){
        super("Resource is already active");
    }

    public ResourceAlreadyActiveException(String message){
        super(message);
    }

    public ResourceAlreadyActiveException(Long id){
        super("Resource with ID " + id + " is already active");
    }

    public ResourceAlreadyActiveException(String resource, Long id){
        super(resource + " with ID " + id + " is already active");
    }
}
