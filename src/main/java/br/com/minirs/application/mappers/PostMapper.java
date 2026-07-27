package br.com.minirs.application.mappers;

import br.com.minirs.application.dtos.post.PostCreateRequest;
import br.com.minirs.application.dtos.post.PostResponse;
import br.com.minirs.domain.entities.Post;
import br.com.minirs.domain.entities.User;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    private final UserMapper userMapper;

    public PostMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Post toEntity(PostCreateRequest request, User postOwner) {
        return new Post(request.content(), postOwner);
    }

    public PostResponse toResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                userMapper.toResponse(post.getPostOwner()),
                post.getLikeCount(),
                post.getPublishedAt()
        );
    }
}
