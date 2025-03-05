package br.com.minirs.services;

import br.com.minirs.dto.reactions.DtoCreateComment;
import br.com.minirs.dto.reactions.DtoReturnComment;
import br.com.minirs.dto.reactions.DtoUpdateComment;
import br.com.minirs.entities.Comments;
import br.com.minirs.entities.PrivacyStatusEnum;
import br.com.minirs.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Transactional
    public ResponseEntity<?> createComment(DtoCreateComment data) {
        if (!userService.existsById(data.userId()) || !postService.existsById(data.postId())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User or Post not found.");
        }

        var user = userService.getReferenceById(data.userId());
        var post = postService.getReferenceById(data.postId());

        if (!user.getFollowing().contains(post.getPostOwner()) && post.getPostOwner().getProfilePrivacyStatus().equals(PrivacyStatusEnum.PRIVATE)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User is not following post owner.");
        }

        var comment = new Comments(user, post, data.content());
        this.save(comment);

        return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnComment(comment));
    }

    @Transactional
    public ResponseEntity<?> deleteComment(Long commentId, Long userId) {
        if (this.existsById(commentId)){
            var comment = this.getReferenceById(commentId);
            var user = userService.getReferenceById(userId);

            if (comment.getUser() != user) return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is not allowed to delete this comment.");

            if (comment.getActive()){
                comment.setActive(false);
                this.save(comment);

                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnComment(comment));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Comment is already deleted.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Comment not found.");
    }

    @Transactional
    public ResponseEntity<?> reactiveComment(Long commentId, Long userId) {
        if (this.existsById(commentId)){
            var comment = this.getReferenceById(commentId);
            var user = userService.getReferenceById(userId);

            if (comment.getUser() != user) return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is not allowed to reactivate this comment.");

            if (!comment.getActive()){
                comment.setActive(true);
                this.save(comment);

                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnComment(comment));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Comment is already active.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Comment not found.");
    }

    @Transactional
    public ResponseEntity<?> updateComment(DtoUpdateComment data) {
        if (this.existsById(data.commentId())){
            var comment = this.getReferenceById(data.commentId());
            var user = userService.getReferenceById(data.userId());

            if (comment.getUser() != user) return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is not allowed to update this comment.");

            if (comment.getActive()){
                comment.updateComment(data.content());
                this.save(comment);

                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnComment(comment));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Comment is deleted.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Comment not found.");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getCommentById(Long id) {
        if (this.existsById(id)){
            var comment = this.getReferenceById(id);

            if (!comment.getActive()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Comment was deleted.");
            }

            return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnComment(comment));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Comment not found.");
    }

    @Transactional
    private void save(Comments comment) {
        commentRepository.save(comment);
    }

    private boolean existsById(Long id) {
        return commentRepository.existsById(id);
    }

    private Comments getReferenceById(Long id) {
        return commentRepository.getReferenceById(id);
    }
}
