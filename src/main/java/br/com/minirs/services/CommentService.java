package br.com.minirs.services;

import br.com.minirs.dto.reactions.DtoCreateComment;
import br.com.minirs.dto.reactions.DtoReturnComment;
import br.com.minirs.dto.reactions.DtoUpdateComment;
import br.com.minirs.entities.Comments;
import br.com.minirs.entities.PrivacyStatusEnum;
import br.com.minirs.repositories.CommentRepository;
import br.com.minirs.repositories.PostRepository;
import br.com.minirs.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Transactional
    public ResponseEntity<?> commentPost(DtoCreateComment data) {
        if (!userRepository.existsById(data.userId()) || !postRepository.existsById(data.postId())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User or Post not found.");
        }

        var user = userRepository.getReferenceById(data.userId());
        var post = postRepository.getReferenceById(data.postId());

        if (!user.getFollowing().contains(post.getPostOwner()) && post.getPostOwner().getProfilePrivacyStatus().equals(PrivacyStatusEnum.PRIVATE)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User is not following post owner.");
        }

        var comment = new Comments(user, post, data.content());
        commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnComment(comment));
    }

    @Transactional
    public ResponseEntity<?> deleteComment(Long id) {
        if (commentRepository.existsById(id)){
            var comment = commentRepository.getReferenceById(id);
            if (comment.getActive()){
                comment.setActive(false);

                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnComment(comment));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Comment is already deleted.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Comment not found.");
    }

    @Transactional
    public ResponseEntity<?> reactiveComment(Long id) {
        if (commentRepository.existsById(id)){
            var comment = commentRepository.getReferenceById(id);
            if (!comment.getActive()){
                comment.setActive(true);

                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnComment(comment));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Comment is already active.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Comment not found.");
    }

    @Transactional
    public ResponseEntity<?> updateComment(DtoUpdateComment data) {
        if (commentRepository.existsById(data.commentId())){
            var comment = commentRepository.getReferenceById(data.commentId());
            if (comment.getActive()){
                comment.updateComment(data.content());

                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnComment(comment));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Comment is deleted.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Comment not found.");
    }

    @Transactional
    public ResponseEntity<?> getCommentById(Long id) {
        if (commentRepository.existsById(id)){
            var comment = commentRepository.getReferenceById(id);

            if (!comment.getActive()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Comment was deleted.");
            }

            return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnComment(comment));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Comment not found.");
    }
}
