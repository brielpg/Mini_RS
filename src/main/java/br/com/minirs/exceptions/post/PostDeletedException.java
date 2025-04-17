package br.com.minirs.exceptions.post;

public class PostDeletedException extends RuntimeException {
    public PostDeletedException(){
        super("Post deleted");
    }

    public PostDeletedException(String message){
        super(message);
    }
}
