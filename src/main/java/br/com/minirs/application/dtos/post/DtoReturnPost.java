package br.com.minirs.application.dtos.post;

import br.com.minirs.application.dtos.reactions.DtoReturnComment;
import br.com.minirs.application.dtos.user.DtoReturnUser;
import br.com.minirs.domain.entities.Comments;
import br.com.minirs.domain.entities.Post;

import java.time.LocalDate;
import java.util.List;

public class DtoReturnPost {
    public Long id;
    public String content;
    public LocalDate publishDate;
    public DtoReturnUser postOwner;
    public List<DtoReturnComment> comments;
    public Integer likeCount;

    public DtoReturnPost(Post post){
        this.id = post.getId();
        this.content = post.getContent();
        this.publishDate = post.getPublishDate();
        this.likeCount = post.getLikeCount();
        this.comments = post.getComment().stream().filter(Comments::getActive).map(DtoReturnComment::new).toList();
        this.postOwner = new DtoReturnUser(post.getPostOwner());
    }
}
