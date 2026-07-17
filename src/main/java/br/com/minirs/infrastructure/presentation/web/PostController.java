package br.com.minirs.infrastructure.presentation.web;

import br.com.minirs.application.dtos.post.PostCreateRequest;
import br.com.minirs.application.dtos.post.PostResponse;
import br.com.minirs.application.dtos.post.PostUpdateRequest;
import br.com.minirs.application.dtos.reactions.LikeRequest;
import br.com.minirs.application.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id){
        var post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getPostsByUser(@PathVariable Long userId){
        var posts = postService.getPostsByUser(userId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/feed/{userId}")
    public ResponseEntity<List<PostResponse>> getFollowingUsersPosts(@PathVariable Long userId){
        var posts = postService.getFollowingUsersPosts(userId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostResponse>> getPublicFeed(){
        var posts = postService.getPublicFeed();
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody @Valid PostCreateRequest data){
        var post = postService.createPost(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping
    public ResponseEntity<PostResponse> updatePost(@RequestBody @Valid PostUpdateRequest data){
        var post = postService.updatePost(data);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{postId}/{userId}")
    public ResponseEntity<PostResponse> deletePost(@PathVariable Long postId, @PathVariable Long userId){
        var post = postService.deletePost(postId, userId);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/reactivate/{postId}/{userId}")
    public ResponseEntity<PostResponse> reactivatePost(@PathVariable Long postId, @PathVariable Long userId){
        var post = postService.reactivatePost(postId, userId);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/like")
    public ResponseEntity<PostResponse> likePost(@RequestBody @Valid LikeRequest data){
        var post = postService.likePost(data);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/dislike")
    public ResponseEntity<PostResponse> dislikePost(@RequestBody @Valid LikeRequest data){
        var post = postService.dislikePost(data);
        return ResponseEntity.ok(post);
    }
}
