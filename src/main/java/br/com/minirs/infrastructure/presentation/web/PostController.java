package br.com.minirs.infrastructure.presentation.web;

import br.com.minirs.application.dtos.post.PostCreateRequest;
import br.com.minirs.application.dtos.post.PostResponse;
import br.com.minirs.application.dtos.post.PostUpdateRequest;
import br.com.minirs.application.dtos.reactions.LikeRequest;
import br.com.minirs.application.mappers.PostMapper;
import br.com.minirs.application.services.PostService;
import br.com.minirs.domain.entities.Post;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;

    public PostController(PostService postService, PostMapper postMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id){
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(postMapper.toResponse(post));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getPostsByUser(@PathVariable Long userId){
        List<Post> posts = postService.getPostsByUser(userId);
        return ResponseEntity.ok(posts.stream().map(postMapper::toResponse).toList());
    }

    @GetMapping("/feed/{userId}")
    public ResponseEntity<List<PostResponse>> getFollowingUsersPosts(@PathVariable Long userId){
        List<Post> posts = postService.getFollowingUsersPosts(userId);
        return ResponseEntity.ok(posts.stream().map(postMapper::toResponse).toList());
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostResponse>> getPublicFeed(){
        List<Post> posts = postService.getPublicFeed();
        return ResponseEntity.ok(posts.stream().map(postMapper::toResponse).toList());
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody @Valid PostCreateRequest data){
        Post post = postService.createPost(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(postMapper.toResponse(post));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long userId, @RequestBody @Valid PostUpdateRequest data){
        Post post = postService.updatePost(data, userId);
        return ResponseEntity.ok(postMapper.toResponse(post));
    }

    @DeleteMapping("/{postId}/{userId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, @PathVariable Long userId){
        postService.deletePost(postId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reactivate/{postId}/{userId}")
    public ResponseEntity<PostResponse> reactivatePost(@PathVariable Long postId, @PathVariable Long userId){
        Post post = postService.reactivatePost(postId, userId);
        return ResponseEntity.ok(postMapper.toResponse(post));
    }

    @PostMapping("/like")
    public ResponseEntity<PostResponse> likePost(@RequestBody @Valid LikeRequest data){
        Post post = postService.likePost(data);
        return ResponseEntity.ok(postMapper.toResponse(post));
    }

    @DeleteMapping("/dislike")
    public ResponseEntity<PostResponse> dislikePost(@RequestBody @Valid LikeRequest data){
        Post post = postService.dislikePost(data);
        return ResponseEntity.ok(postMapper.toResponse(post));
    }
}
