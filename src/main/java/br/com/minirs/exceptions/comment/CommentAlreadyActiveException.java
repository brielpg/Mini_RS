package br.com.minirs.exceptions.comment;

public class CommentAlreadyActiveException extends RuntimeException {
    public CommentAlreadyActiveException() {
        super("Comment is already active.");
    }
}
