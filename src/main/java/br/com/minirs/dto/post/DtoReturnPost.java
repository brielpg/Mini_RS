package br.com.minirs.dto.post;

import br.com.minirs.dto.user.DtoReturnUser;
import br.com.minirs.entities.Post;

import java.time.LocalDate;

public class DtoReturnPost {
    public Long id;
    public String content;
    public LocalDate publishDate;
    public DtoReturnUser postOwner;
    public Integer likeCount;

    public DtoReturnPost(Post post){
        this.id = post.getId();
        this.content = post.getContent();
        this.publishDate = post.getPublishDate();
        this.likeCount = post.getLikeCount();
        this.postOwner = new DtoReturnUser(post.getPostOwner());
    }
}
