package br.com.minirs.exceptions;

public class ResourceDisabledException extends RuntimeException{
    public ResourceDisabledException(){
        super("Resource is disabled");
    }

    public ResourceDisabledException(String message){
        super(message);
    }

    public ResourceDisabledException(Long id){
        super("Resource with ID " + id + " is disabled");
    }

    public ResourceDisabledException(String resource, Long id){
        super(resource + " with ID " + id + " is disabled");
    }
}
