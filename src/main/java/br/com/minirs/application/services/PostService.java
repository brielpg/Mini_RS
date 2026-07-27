package br.com.minirs.application.services;

import br.com.minirs.application.dtos.post.PostCreateRequest;
import br.com.minirs.application.dtos.post.PostUpdateRequest;
import br.com.minirs.application.dtos.reactions.LikeRequest;
import br.com.minirs.application.mappers.PostMapper;
import br.com.minirs.domain.entities.Post;
import br.com.minirs.domain.entities.User;
import br.com.minirs.domain.exceptions.NotFoundException;
import br.com.minirs.domain.exceptions.ResourceDisabledException;
import br.com.minirs.domain.exceptions.UnauthorizedException;
import br.com.minirs.infrastructure.repositories.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository repository;
    private final PostMapper mapper;
    private final UserService userService;

    private static final String NOT_ALLOWED = "User is not allowed to perform this action";

    public PostService(PostRepository repository, PostMapper mapper, UserService userService) {
        this.repository = repository;
        this.mapper = mapper;
        this.userService = userService;
    }

    @Transactional
    public Post createPost(PostCreateRequest data) {
        userService.validateUserExistsById(data.userId());

        User user = userService.getReferenceById(data.userId());
        Post newPost = mapper.toEntity(data, user);

        this.save(newPost);

        return newPost;
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        validatePostExistsById(postId);

        Post post = this.getReferenceById(postId);
        validatePostActive(post);

        User user = userService.getReferenceById(userId);
        validatePostOwner(post, user, NOT_ALLOWED);

        post.delete();
        this.save(post);
    }

    @Transactional
    public Post reactivatePost(Long postId, Long userId) {
        validatePostExistsById(postId);

        Post post = this.getReferenceById(postId);
        User user = userService.getReferenceById(userId);

        validatePostOwner(post, user, NOT_ALLOWED);

        post.reactivate();
        this.save(post);

        return post;
    }

    @Transactional
    public Post updatePost(PostUpdateRequest data, Long userId) {
        validatePostExistsById(data.postId());

        Post post = this.getReferenceById(data.postId());
        User user = userService.getReferenceById(userId);

        validatePostOwner(post, user, "User is not allowed to update this post");

        validatePostActive(post);

        post.changeContent(data.content());
        this.save(post);

        return post;
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByUser(Long userId) {
        userService.validateUserExistsById(userId);
        User user = userService.getReferenceById(userId);
        userService.validateUserActive(user);

        return repository.findActivePostsByPostOwner(user);
    }

    @Transactional(readOnly = true)
    public List<Post> getFollowingUsersPosts(Long userId) {
        userService.validateUserExistsById(userId);
        User user = userService.getReferenceById(userId);
        userService.validateUserActive(user);

        return userService.getFollowingList(userId).stream()
                .flatMap(i -> repository.findActivePostsByPostOwner(i).stream())
                .toList();
    }

    @Transactional(readOnly = true)
    public Post getPostById(Long id) {
        validatePostExistsById(id);

        Post post = this.getReferenceById(id);

        validatePostActive(post);

        return post;
    }

    @Transactional
    public Post likePost(LikeRequest data) {
        userService.validateUserExistsById(data.userId());
        validatePostExistsById(data.postId());

        Post post = this.getReferenceById(data.postId());

        validatePostActive(post);

        User user = userService.getReferenceById(data.userId());

        post.likeBy(user.getId());
        this.save(post);

        return post;
    }

    @Transactional
    public Post dislikePost(LikeRequest data) {
        userService.validateUserExistsById(data.userId());
        validatePostExistsById(data.postId());

        Post post = this.getReferenceById(data.postId());

        validatePostActive(post);

        User user = userService.getReferenceById(data.userId());

        post.dislikeBy(user.getId());
        this.save(post);

        return post;
    }

    @Transactional(readOnly = true)
    public List<Post> getPublicFeed() {
        return repository.findAllPostsWherePostOwnerProfileIsPublic();
    }

    public void validatePostExistsById(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Post", id);
        }
    }

    private void validatePostOwner(Post post, User user, String message){
        if (post.getPostOwner() != user){
            throw new UnauthorizedException(message);
        }
    }

    private void validatePostActive(Post post) {
        if (!post.isActive()) {
            throw new ResourceDisabledException("Post is already deactivated");
        }
    }

    public Post getReferenceById(Long id) {
        return repository.getReferenceById(id);
    }

    @Transactional
    private void save(Post post) {
        repository.save(post);
    }
}
