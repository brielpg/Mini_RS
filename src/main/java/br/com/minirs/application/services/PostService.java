package br.com.minirs.application.services;

import br.com.minirs.application.dtos.post.PostCreateRequest;
import br.com.minirs.application.dtos.post.PostResponse;
import br.com.minirs.application.dtos.post.PostUpdateRequest;
import br.com.minirs.application.dtos.reactions.LikeRequest;
import br.com.minirs.domain.entities.Post;
import br.com.minirs.domain.entities.User;
import br.com.minirs.domain.exceptions.NotFoundException;
import br.com.minirs.domain.exceptions.ResourceAlreadyActiveException;
import br.com.minirs.domain.exceptions.ResourceDisabledException;
import br.com.minirs.domain.exceptions.UnauthorizedException;
import br.com.minirs.domain.exceptions.post.LikedPostsException;
import br.com.minirs.infrastructure.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;


    @Transactional
    public PostResponse createPost(PostCreateRequest data) {
        userService.validateUserExistsById(data.userId());

        var user = userService.getReferenceById(data.userId());
        user.setPostCount(user.getPostCount() + 1);
        var newPost = new Post(data, user);

        this.save(newPost);

        return new PostResponse(newPost);
    }

    @Transactional
    public PostResponse deletePost(Long postId, Long userId) {
        validatePostExistsById(postId);

        var post = this.getReferenceById(postId);
        var user = userService.getReferenceById(userId);

        validatePostOwner(post,user,"User is not allowed to delete this post");

        validatePostActive(post);

        post.setActive(false);
        user.setPostCount(user.getPostCount() - 1);

        userService.save(user);
        this.save(post);

        return new PostResponse(post);
    }

    @Transactional
    public PostResponse reactivatePost(Long postId, Long userId) {
        validatePostExistsById(postId);

        var post = this.getReferenceById(postId);
        var user = userService.getReferenceById(userId);

        validatePostOwner(post, user, "User is not allowed to reactivate this post");

        if (post.getActive()) throw new ResourceAlreadyActiveException("Post", postId);

        post.setActive(true);
        user.setPostCount(user.getPostCount() + 1);

        userService.save(user);
        this.save(post);

        return new PostResponse(post);
    }

    @Transactional
    public PostResponse updatePost(PostUpdateRequest data) {
        validatePostExistsById(data.postId());

        var post = this.getReferenceById(data.postId());
        var user = userService.getReferenceById(data.userId());

        validatePostOwner(post, user, "User is not allowed to update this post");

        validatePostActive(post);

        post.updatePost(data.content());
        this.save(post);

        return new PostResponse(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUser(Long userId) {
        userService.validateUserExistsById(userId);
        var user = userService.getReferenceById(userId);
        userService.validateUserActive(user);

        var postsByOwner = postRepository.findActivePostsByPostOwner(user);
        return postsByOwner.stream()
                .map(PostResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getFollowingUsersPosts(Long userId) {
        userService.validateUserExistsById(userId);
        var user = userService.getReferenceById(userId);
        userService.validateUserActive(user);

        return user.getFollowing().stream()
                .flatMap(i -> postRepository.findActivePostsByPostOwner(i).stream())
                .map(PostResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        validatePostExistsById(id);

        var post = this.getReferenceById(id);

        validatePostActive(post);

        return new PostResponse(post);
    }

    @Transactional
    public PostResponse likePost(LikeRequest data) {
        userService.validateUserExistsById(data.userId());
        validatePostExistsById(data.postId());

        var post = this.getReferenceById(data.postId());

        validatePostActive(post);

        var user = userService.getReferenceById(data.userId());

        for (var i : post.getLikesByUserId()) {
            if (i.equals(user.getId())) {
                throw new LikedPostsException("User already liked this post");
            }
        }

        post.getLikesByUserId().add(data.userId());
        post.setLikeCount(post.getLikeCount() + 1);
        this.save(post);

        return new PostResponse(post);
    }

    @Transactional
    public PostResponse dislikePost(LikeRequest data) {
        userService.validateUserExistsById(data.userId());
        validatePostExistsById(data.postId());

        var post = this.getReferenceById(data.postId());

        validatePostActive(post);

        var user = userService.getReferenceById(data.userId());

        for (var i : post.getLikesByUserId()) {
            if (i.equals(user.getId())) {
                post.getLikesByUserId().remove(i);
                post.setLikeCount(post.getLikeCount() - 1);
                this.save(post);

                return new PostResponse(post);
            }
        }
        throw new LikedPostsException("User didn't like this post.");

    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPublicFeed() {
        return postRepository.findAllPostsWherePostOwnerProfileIsPublic().stream()
                .map(PostResponse::new)
                .toList();
    }

    public void validatePostExistsById(Long id) {
        if (!postRepository.existsById(id)) {
            throw new NotFoundException("Post", id);
        }
    }

    private void validatePostOwner(Post post, User user, String message){
        if (post.getPostOwner() != user){
            throw new UnauthorizedException(message);
        }
    }

    private void validatePostActive(Post post) {
        if (!post.getActive()) {
            throw new ResourceDisabledException("Post", post.getId());
        }
    }

    public Post getReferenceById(Long id) {
        return postRepository.getReferenceById(id);
    }

    @Transactional
    private void save(Post post) {
        postRepository.save(post);
    }
}
