package br.com.minirs.services;

import br.com.minirs.dto.post.DtoCreatePost;
import br.com.minirs.dto.post.DtoReturnPost;
import br.com.minirs.dto.post.DtoUpdatePost;
import br.com.minirs.dto.reactions.DtoLike;
import br.com.minirs.entities.Post;
import br.com.minirs.repositories.PostRepository;
import br.com.minirs.repositories.UserRepository;
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
    private UserRepository userRepository;


    @Transactional
    public ResponseEntity<?> createPost(DtoCreatePost data) {
        if (!userRepository.existsById(data.userId())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
        }

        var user = userRepository.getReferenceById(data.userId());
        user.setPostCount(user.getPostCount()+1);
        var newPost = new Post(data, user);

        postRepository.save(newPost);

        return ResponseEntity.status(HttpStatus.CREATED).body(new DtoReturnPost(newPost));
    }

    @Transactional
    public ResponseEntity<?> deletePost(Long id) {
        if (postRepository.existsById(id)){
            var post = postRepository.getReferenceById(id);
            if (post.getActive()){
                post.setActive(false);

                var user = userRepository.getReferenceById(post.getPostOwner().getId());
                user.setPostCount(user.getPostCount()-1);

                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnPost(post));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Post is already deleted.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Post not found.");
    }

    @Transactional
    public ResponseEntity<?> reactivatePost(Long id) {
        if (postRepository.existsById(id)){
            var post = postRepository.getReferenceById(id);
            if (!post.getActive()){
                post.setActive(true);

                var user = userRepository.getReferenceById(post.getPostOwner().getId());
                user.setPostCount(user.getPostCount()+1);

                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnPost(post));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Post is already active.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Post not found.");
    }

    @Transactional
    public ResponseEntity<?> updatePost(DtoUpdatePost data) {
        if (postRepository.existsById(data.postId())){
            var post = postRepository.getReferenceById(data.postId());
            if (post.getActive()){
                post.updatePost(data.content());

                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnPost(post));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Post is deleted.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Post not found.");
    }

    @Transactional
    public ResponseEntity<?> getPostsByUser(Long userId) {
        if (userRepository.existsById(userId)){
            var user = userRepository.getReferenceById(userId);
            if (user.getActive()){
                var postsByOwner = postRepository.findByPostOwner(user);
                List<DtoReturnPost> posts = postsByOwner.stream()
                        .filter(Post::getActive)
                        .map(DtoReturnPost::new).toList();

                return ResponseEntity.status(HttpStatus.OK).body(posts);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is disabled.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
    }

    @Transactional
    public ResponseEntity<?> getFollowingUsersPosts(Long userId) {
        if (userRepository.existsById(userId)){
            var user = userRepository.getReferenceById(userId);
            if (user.getActive()){
                var posts = user.getFollowing().stream()
                        .flatMap(i -> postRepository.findByPostOwner(i).stream())
                        .filter(Post::getActive)
                        .map(DtoReturnPost::new).toList();

                return ResponseEntity.status(HttpStatus.OK).body(posts);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is disabled.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
    }

    @Transactional
    public ResponseEntity<?> getPostById(Long id) {
        if (postRepository.existsById(id)){
            var post = postRepository.getReferenceById(id);

            if (!post.getActive()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Post was deleted.");
            }

            return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnPost(post));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Post not found.");
    }

    @Transactional
    public ResponseEntity<?> likePost(DtoLike data) {
        if (!userRepository.existsById(data.userId()) || !postRepository.existsById(data.postId())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User or Post not found.");
        }

        var post = postRepository.getReferenceById(data.postId());

        if (!post.getActive()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Post was deleted.");
        }

        var user = userRepository.getReferenceById(data.userId());

        for (var i: post.getLikesByUserId()){
            if (i.equals(user.getId())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User already liked this post");
            }
        }

        post.getLikesByUserId().add(data.userId());
        post.setLikeCount(post.getLikeCount()+1);

        return ResponseEntity.status(HttpStatus.OK).body(user.getUserName() + " LIKED THE POST");
    }

    @Transactional
    public ResponseEntity<?> dislikePost(DtoLike data) {
        if (!userRepository.existsById(data.userId()) || !postRepository.existsById(data.postId())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User or Post not found.");
        }

        var post = postRepository.getReferenceById(data.postId());

        if (!post.getActive()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Post was deleted.");
        }

        var user = userRepository.getReferenceById(data.userId());

        for (var i: post.getLikesByUserId()){
            if (i.equals(user.getId())){
                post.getLikesByUserId().remove(i);
                post.setLikeCount(post.getLikeCount()-1);
                
                return ResponseEntity.status(HttpStatus.OK).body("LIKE REMOVED");
            }
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User didn't like this post.");

    }

    @Transactional
    public ResponseEntity<?> getPublicFeed() {
        var publicPosts = postRepository.findAllPostsWherePostOwnerProfileIsPublic().stream()
                .map(DtoReturnPost::new)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(publicPosts);
    }
}
