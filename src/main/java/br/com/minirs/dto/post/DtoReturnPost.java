package br.com.minirs.dto.post;

import br.com.minirs.dto.reactions.DtoReturnComment;
import br.com.minirs.dto.user.DtoReturnUser;
import br.com.minirs.entities.Comments;
import br.com.minirs.entities.Post;

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
