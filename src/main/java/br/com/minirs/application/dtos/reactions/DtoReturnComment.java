package br.com.minirs.application.dtos.reactions;

import br.com.minirs.domain.entities.Comments;

import java.time.LocalDate;
import java.util.List;

public class DtoReturnComment {
    public Long id;
    public String content;
    public String commentOwner;
    public Long userId;
    public Long postId;
    public LocalDate publishDate;
    public Boolean commentUpdated;
    public List<LocalDate> updateDates;

    public DtoReturnComment(Comments comment){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.commentOwner = comment.getUser().getUserName();
        this.userId = comment.getUser().getId();
        this.postId = comment.getPost().getId();
        this.publishDate = comment.getPublishDate();
        this.commentUpdated = comment.getUpdated();
        this.updateDates = comment.getUpdateDates();
    }
}
