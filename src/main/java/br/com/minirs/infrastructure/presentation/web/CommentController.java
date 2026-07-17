package br.com.minirs.infrastructure.presentation.web;

import br.com.minirs.application.dtos.reactions.DtoCreateComment;
import br.com.minirs.application.dtos.reactions.DtoReturnComment;
import br.com.minirs.application.dtos.reactions.DtoUpdateComment;
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
    public ResponseEntity<DtoReturnComment> getCommentById(@PathVariable Long id){
        var comment = commentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }

    @PostMapping
    public ResponseEntity<DtoReturnComment> createComment(@RequestBody @Valid DtoCreateComment data){
        var comment = commentService.createComment(data);
        return ResponseEntity.ok(comment);
    }

    @PutMapping
    public ResponseEntity<DtoReturnComment> updateComment(@RequestBody @Valid DtoUpdateComment data){
        var comment = commentService.updateComment(data);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{commentId}/{userId}")
    public ResponseEntity<DtoReturnComment> deleteComment(@PathVariable Long commentId, @PathVariable Long userId){
        var comment = commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/reactive/{commentId}/{userId}")
    public ResponseEntity<DtoReturnComment> reactiveComment(@PathVariable Long commentId, @PathVariable Long userId){
        var comment = commentService.reactiveComment(commentId, userId);
        return ResponseEntity.ok(comment);
    }
}

