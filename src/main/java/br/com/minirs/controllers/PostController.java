package br.com.minirs.controllers;

import br.com.minirs.dto.post.DtoCreatePost;
import br.com.minirs.dto.post.DtoUpdatePost;
import br.com.minirs.dto.reactions.DtoLike;
import br.com.minirs.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id){
        return postService.getPostById(id);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostsByUser(@PathVariable Long userId){
        return postService.getPostsByUser(userId);
    }

    @GetMapping("/feed/{userId}")
    public ResponseEntity<?> getFollowingUsersPosts(@PathVariable Long userId){
        return postService.getFollowingUsersPosts(userId);
    }

    @GetMapping("/feed")
    public ResponseEntity<?> getPublicFeed(){
        return postService.getPublicFeed();
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody @Valid DtoCreatePost data){
        return postService.createPost(data);
    }

    @PutMapping
    public ResponseEntity<?> updatePost(@RequestBody @Valid DtoUpdatePost data){
        return postService.updatePost(data);
    }

    @DeleteMapping("/{postId}/{userId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @PathVariable Long userId){
        return postService.deletePost(postId, userId);
    }

    @DeleteMapping("/reactivate/{postId}/{userId}")
    public ResponseEntity<?> reactivatePost(@PathVariable Long postId, @PathVariable Long userId){
        return postService.reactivatePost(postId, userId);
    }

    @PostMapping("/like")
    public ResponseEntity<?> likePost(@RequestBody @Valid DtoLike data){
        return postService.likePost(data);
    }

    @DeleteMapping("/dislike")
    public ResponseEntity<?> dislikePost(@RequestBody @Valid DtoLike data){
        return postService.dislikePost(data);
    }
}
