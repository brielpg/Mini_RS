package br.com.minirs.services;

import br.com.minirs.dto.post.DtoCreatePost;
import br.com.minirs.dto.post.DtoReturnPost;
import br.com.minirs.dto.post.DtoUpdatePost;
import br.com.minirs.dto.reactions.DtoLike;
import br.com.minirs.entities.Post;
import br.com.minirs.entities.User;
import br.com.minirs.exceptions.post.LikedPostsException;
import br.com.minirs.exceptions.post.PostDeletedException;
import br.com.minirs.exceptions.post.PostNotFoundException;
import br.com.minirs.exceptions.post.UnauthorizedUserException;
import br.com.minirs.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createPost(DtoCreatePost data) {
        userService.validateUserExistsById(data.userId());

        var user = userService.getReferenceById(data.userId());
        user.setPostCount(user.getPostCount() + 1);
        var newPost = new Post(data, user);

        this.save(newPost);

        return ResponseEntity.status(HttpStatus.CREATED).body(new DtoReturnPost(newPost));
    }

    @Transactional
    public ResponseEntity<?> deletePost(Long postId, Long userId) {
        validatePostExistsById(postId);

        var post = this.getReferenceById(postId);
        var user = userService.getReferenceById(userId);

        validatePostOwner(post,user,"User is not allowed to delete this post");

        validatePostActive(post);

        post.setActive(false);
        user.setPostCount(user.getPostCount() - 1);

        userService.save(user);
        this.save(post);

        return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnPost(post));
    }

    @Transactional
    public ResponseEntity<?> reactivatePost(Long postId, Long userId) {
        validatePostExistsById(postId);

        var post = this.getReferenceById(postId);
        var user = userService.getReferenceById(userId);

        validatePostOwner(post, user, "User is not allowed to reactivate this post");

        if (post.getActive()) throw new PostDeletedException("Post is already active");

        post.setActive(true);
        user.setPostCount(user.getPostCount() + 1);

        userService.save(user);
        this.save(post);

        return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnPost(post));
    }

    @Transactional
    public ResponseEntity<?> updatePost(DtoUpdatePost data) {
        validatePostExistsById(data.postId());

        var post = this.getReferenceById(data.postId());
        var user = userService.getReferenceById(data.userId());

        validatePostOwner(post, user, "User is not allowed to update this post");

        validatePostActive(post);

        post.updatePost(data.content());
        this.save(post);

        return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnPost(post));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getPostsByUser(Long userId) {
        userService.validateUserExistsById(userId);
        var user = userService.getReferenceById(userId);
        userService.validateUserActive(user);

        var postsByOwner = postRepository.findActivePostsByPostOwner(user);
        List<DtoReturnPost> posts = postsByOwner.stream()
                .map(DtoReturnPost::new)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getFollowingUsersPosts(Long userId) {
        userService.validateUserExistsById(userId);
        var user = userService.getReferenceById(userId);
        userService.validateUserActive(user);

        var posts = user.getFollowing().stream()
                .flatMap(i -> postRepository.findActivePostsByPostOwner(i).stream())
                .map(DtoReturnPost::new)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getPostById(Long id) {
        validatePostExistsById(id);

        var post = this.getReferenceById(id);

        validatePostActive(post);

        return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnPost(post));
    }

    @Transactional
    public ResponseEntity<?> likePost(DtoLike data) {
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

        return ResponseEntity.status(HttpStatus.OK).body(user.getUserName() + " LIKED THE POST");
    }

    @Transactional
    public ResponseEntity<?> dislikePost(DtoLike data) {
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

                return ResponseEntity.status(HttpStatus.OK).body("LIKE REMOVED");
            }
        }
        throw new LikedPostsException("User didn't like this post.");

    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getPublicFeed() {
        var publicPosts = postRepository.findAllPostsWherePostOwnerProfileIsPublic().stream()
                .map(DtoReturnPost::new)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(publicPosts);
    }

    public void validatePostExistsById(Long id) {
        if (!postRepository.existsById(id)) {
            throw new PostNotFoundException(id);
        }
    }

    private void validatePostOwner(Post post, User user, String message){
        if (post.getPostOwner() != user){
            throw new UnauthorizedUserException(message);
        }
    }

    private void validatePostActive(Post post) {
        if (!post.getActive()) {
            throw new PostDeletedException();
        }
    }

    public boolean existsById(Long id) {
        return postRepository.existsById(id);
    }

    public Post getReferenceById(Long id) {
        return postRepository.getReferenceById(id);
    }

    @Transactional
    private void save(Post post) {
        postRepository.save(post);
    }
}
