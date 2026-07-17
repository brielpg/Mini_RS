package br.com.minirs.application.dtos.post;

import br.com.minirs.application.dtos.reactions.CommentResponse;
import br.com.minirs.application.dtos.user.UserResponse;
import br.com.minirs.domain.entities.Comments;
import br.com.minirs.domain.entities.Post;

import java.time.LocalDate;
import java.util.List;

public class PostResponse {
    public Long id;
    public String content;
    public LocalDate publishDate;
    public UserResponse postOwner;
    public List<CommentResponse> comments;
    public Integer likeCount;

    public PostResponse(Post post){
        this.id = post.getId();
        this.content = post.getContent();
        this.publishDate = post.getPublishDate();
        this.likeCount = post.getLikeCount();
        this.comments = post.getComment().stream().filter(Comments::getActive).map(CommentResponse::new).toList();
        this.postOwner = new UserResponse(post.getPostOwner());
    }
}
