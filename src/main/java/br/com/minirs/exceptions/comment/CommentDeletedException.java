package br.com.minirs.exceptions.comment;

public class CommentDeletedException extends RuntimeException {
    public CommentDeletedException() {
        super("Comment has been deleted and cannot be modified.");
    }
}
