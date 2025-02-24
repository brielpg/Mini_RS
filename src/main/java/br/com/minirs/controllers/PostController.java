package br.com.minirs.controllers;

import br.com.minirs.dto.post.DtoCreatePost;
import br.com.minirs.dto.post.DtoUpdatePost;
import br.com.minirs.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<?> getPostById(@PathVariable Long id){
        return postService.getPostById(id);
    }

    @GetMapping("/user/{userId}")
    @Transactional
    public ResponseEntity<?> getPostsByUser(@PathVariable Long userId){
        return postService.getPostsByUser(userId);
    }

    @GetMapping("/feed/{userId}")
    @Transactional
    public ResponseEntity<?> getFollowingUsersPosts(@PathVariable Long userId){
        return postService.getFollowingUsersPosts(userId);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> createPost(@RequestBody @Valid DtoCreatePost data){
        return postService.createPost(data);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<?> updatePost(@RequestBody @Valid DtoUpdatePost data){
        return postService.updatePost(data);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletePost(@PathVariable Long id){
        return postService.deletePost(id);
    }

    @DeleteMapping("/reactivate/{id}")
    @Transactional
    public ResponseEntity<?> reactivatePost(@PathVariable Long id){
        return postService.reactivatePost(id);
    }
}
