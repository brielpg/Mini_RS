package br.com.minirs.exceptions.post;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Long id) {
        super("Post with ID "+id+" not found");
    }

    public PostNotFoundException(){
        super("Post not found");
    }
}
