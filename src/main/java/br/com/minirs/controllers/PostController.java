package br.com.minirs.controllers;

import br.com.minirs.dto.post.DtoCreatePost;
import br.com.minirs.dto.post.DtoReturnPost;
import br.com.minirs.dto.post.DtoUpdatePost;
import br.com.minirs.dto.reactions.DtoLike;
import br.com.minirs.services.PostService;
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
    public ResponseEntity<DtoReturnPost> getPostById(@PathVariable Long id){
        var post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DtoReturnPost>> getPostsByUser(@PathVariable Long userId){
        var posts = postService.getPostsByUser(userId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/feed/{userId}")
    public ResponseEntity<List<DtoReturnPost>> getFollowingUsersPosts(@PathVariable Long userId){
        var posts = postService.getFollowingUsersPosts(userId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/feed")
    public ResponseEntity<List<DtoReturnPost>> getPublicFeed(){
        var posts = postService.getPublicFeed();
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<DtoReturnPost> createPost(@RequestBody @Valid DtoCreatePost data){
        var post = postService.createPost(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping
    public ResponseEntity<DtoReturnPost> updatePost(@RequestBody @Valid DtoUpdatePost data){
        var post = postService.updatePost(data);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{postId}/{userId}")
    public ResponseEntity<DtoReturnPost> deletePost(@PathVariable Long postId, @PathVariable Long userId){
        var post = postService.deletePost(postId, userId);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/reactivate/{postId}/{userId}")
    public ResponseEntity<DtoReturnPost> reactivatePost(@PathVariable Long postId, @PathVariable Long userId){
        var post = postService.reactivatePost(postId, userId);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/like")
    public ResponseEntity<DtoReturnPost> likePost(@RequestBody @Valid DtoLike data){
        var post = postService.likePost(data);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/dislike")
    public ResponseEntity<DtoReturnPost> dislikePost(@RequestBody @Valid DtoLike data){
        var post = postService.dislikePost(data);
        return ResponseEntity.ok(post);
    }
}
