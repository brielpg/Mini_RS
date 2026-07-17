package br.com.minirs.infrastructure.presentation.web;

import br.com.minirs.application.dtos.reactions.CommentCreateRequest;
import br.com.minirs.application.dtos.reactions.CommentResponse;
import br.com.minirs.application.dtos.reactions.CommentUpdateRequest;
import br.com.minirs.application.services.CommentService;
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
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long id){
        var comment = commentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody @Valid CommentCreateRequest data){
        var comment = commentService.createComment(data);
        return ResponseEntity.ok(comment);
    }

    @PutMapping
    public ResponseEntity<CommentResponse> updateComment(@RequestBody @Valid CommentUpdateRequest data){
        var comment = commentService.updateComment(data);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{commentId}/{userId}")
    public ResponseEntity<CommentResponse> deleteComment(@PathVariable Long commentId, @PathVariable Long userId){
        var comment = commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/reactive/{commentId}/{userId}")
    public ResponseEntity<CommentResponse> reactiveComment(@PathVariable Long commentId, @PathVariable Long userId){
        var comment = commentService.reactiveComment(commentId, userId);
        return ResponseEntity.ok(comment);
    }
}

