package br.com.minirs.services;

import br.com.minirs.dto.post.DtoCreatePost;
import br.com.minirs.dto.post.DtoReturnPost;
import br.com.minirs.dto.post.DtoUpdatePost;
import br.com.minirs.dto.reactions.DtoLike;
import br.com.minirs.entities.Post;
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
        if (!userService.existsById(data.userId())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
        }

        var user = userService.getReferenceById(data.userId());
        user.setPostCount(user.getPostCount()+1);
        var newPost = new Post(data, user);

        this.save(newPost);

        return ResponseEntity.status(HttpStatus.CREATED).body(new DtoReturnPost(newPost));
    }

    @Transactional
    public ResponseEntity<?> deletePost(Long postId, Long userId) {
        if (this.existsById(postId)){
            var post = this.getReferenceById(postId);
            var user = userService.getReferenceById(userId);

            if (post.getPostOwner() != user) return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is not allowed to delete this post.");

            if (post.getActive()){
                post.setActive(false);
                user.setPostCount(user.getPostCount()-1);

                userService.save(user);
                this.save(post);

                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnPost(post));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Post is already deleted.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Post not found.");
    }

    @Transactional
    public ResponseEntity<?> reactivatePost(Long postId, Long userId) {
        if (this.existsById(postId)){
            var post = this.getReferenceById(postId);
            var user = userService.getReferenceById(userId);

            if (post.getPostOwner() != user) return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is not allowed to reactivate this post.");

            if (!post.getActive()){
                post.setActive(true);
                user.setPostCount(user.getPostCount()+1);

                userService.save(user);
                this.save(post);

                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnPost(post));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Post is already active.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Post not found.");
    }

    @Transactional
    public ResponseEntity<?> updatePost(DtoUpdatePost data) {
        if (this.existsById(data.postId())){
            var post = this.getReferenceById(data.postId());
            var user = userService.getReferenceById(data.userId());

            if (post.getPostOwner() != user) return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is not allowed to update this post.");

            if (post.getActive()){
                post.updatePost(data.content());
                this.save(post);

                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnPost(post));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Post is deleted.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Post not found.");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getPostsByUser(Long userId) {
        if (userService.existsById(userId)){
            var user = userService.getReferenceById(userId);
            if (user.getActive()){
                var postsByOwner = postRepository.findActivePostsByPostOwner(user);
                List<DtoReturnPost> posts = postsByOwner.stream()
                        .map(DtoReturnPost::new)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(posts);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is disabled.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getFollowingUsersPosts(Long userId) {
        if (userService.existsById(userId)){
            var user = userService.getReferenceById(userId);
            if (user.getActive()){
                var posts = user.getFollowing().stream()
                        .flatMap(i -> postRepository.findActivePostsByPostOwner(i).stream())
                        .map(DtoReturnPost::new)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(posts);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is disabled.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getPostById(Long id) {
        if (this.existsById(id)){
            var post = this.getReferenceById(id);

            if (!post.getActive()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Post was deleted.");
            }

            return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnPost(post));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Post not found.");
    }

    @Transactional
    public ResponseEntity<?> likePost(DtoLike data) {
        if (!userService.existsById(data.userId()) || !this.existsById(data.postId())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User or Post not found.");
        }

        var post = this.getReferenceById(data.postId());

        if (!post.getActive()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Post was deleted.");
        }

        var user = userService.getReferenceById(data.userId());

        for (var i: post.getLikesByUserId()){
            if (i.equals(user.getId())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User already liked this post");
            }
        }

        post.getLikesByUserId().add(data.userId());
        post.setLikeCount(post.getLikeCount()+1);
        this.save(post);

        return ResponseEntity.status(HttpStatus.OK).body(user.getUserName() + " LIKED THE POST");
    }

    @Transactional
    public ResponseEntity<?> dislikePost(DtoLike data) {
        if (!userService.existsById(data.userId()) || !this.existsById(data.postId())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User or Post not found.");
        }

        var post = this.getReferenceById(data.postId());

        if (!post.getActive()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Post was deleted.");
        }

        var user = userService.getReferenceById(data.userId());

        for (var i: post.getLikesByUserId()){
            if (i.equals(user.getId())){
                post.getLikesByUserId().remove(i);
                post.setLikeCount(post.getLikeCount()-1);
                this.save(post);
                
                return ResponseEntity.status(HttpStatus.OK).body("LIKE REMOVED");
            }
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User didn't like this post.");

    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getPublicFeed() {
        var publicPosts = postRepository.findAllPostsWherePostOwnerProfileIsPublic().stream()
                .map(DtoReturnPost::new)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(publicPosts);
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
