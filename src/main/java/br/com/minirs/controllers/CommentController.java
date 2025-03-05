package br.com.minirs.controllers;

import br.com.minirs.dto.reactions.DtoCreateComment;
import br.com.minirs.dto.reactions.DtoUpdateComment;
import br.com.minirs.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long id){
        return commentService.getCommentById(id);
    }

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody @Valid DtoCreateComment data){
        return commentService.createComment(data);
    }

    @PutMapping
    public ResponseEntity<?> updateComment(@RequestBody @Valid DtoUpdateComment data){
        return commentService.updateComment(data);
    }

    @DeleteMapping("/{commentId}/{userId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, @PathVariable Long userId){
        return commentService.deleteComment(commentId, userId);
    }

    @DeleteMapping("/reactive/{commentId}/{userId}")
    public ResponseEntity<?> reactiveComment(@PathVariable Long commentId, @PathVariable Long userId){
        return commentService.reactiveComment(commentId, userId);
    }
}

