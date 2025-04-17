package br.com.minirs.services;

import br.com.minirs.dto.reactions.DtoCreateComment;
import br.com.minirs.dto.reactions.DtoReturnComment;
import br.com.minirs.dto.reactions.DtoUpdateComment;
import br.com.minirs.entities.Comments;
import br.com.minirs.entities.Post;
import br.com.minirs.entities.PrivacyStatusEnum;
import br.com.minirs.entities.User;
import br.com.minirs.exceptions.comment.CommentAlreadyActiveException;
import br.com.minirs.exceptions.comment.CommentDeletedException;
import br.com.minirs.exceptions.comment.CommentNotFoundException;
import br.com.minirs.exceptions.follow.ActionNotAllowedException;
import br.com.minirs.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public DtoReturnComment createComment(DtoCreateComment data) {
        userService.validateUserExistsById(data.userId());
        postService.validatePostExistsById(data.postId());

        var user = userService.getReferenceById(data.userId());
        var post = postService.getReferenceById(data.postId());

        validateUserCanCommentOnPost(user, post);

        var comment = new Comments(user, post, data.content());
        this.save(comment);

        return new DtoReturnComment(comment);
    }

    @Transactional
    public DtoReturnComment deleteComment(Long commentId, Long userId) {
        validateCommentExistsById(commentId);

        var comment = this.getReferenceById(commentId);
        var user = userService.getReferenceById(userId);

        validateUserPermission(comment, user);
        validateCommentActive(comment);

        comment.setActive(false);
        this.save(comment);

        return new DtoReturnComment(comment);
    }

    @Transactional
    public DtoReturnComment reactiveComment(Long commentId, Long userId) {
        validateCommentExistsById(commentId);

        var comment = this.getReferenceById(commentId);
        var user = userService.getReferenceById(userId);

        validateUserPermission(comment, user);
        validateCommentInactive(comment);

        comment.setActive(true);
        this.save(comment);

        return new DtoReturnComment(comment);
    }

    @Transactional
    public DtoReturnComment updateComment(DtoUpdateComment data) {
        validateCommentExistsById(data.commentId());

        var comment = this.getReferenceById(data.commentId());
        var user = userService.getReferenceById(data.userId());

        validateUserPermission(comment, user);
        validateCommentActive(comment);

        comment.updateComment(data.content());
        this.save(comment);

        return new DtoReturnComment(comment);
    }

    @Transactional(readOnly = true)
    public DtoReturnComment getCommentById(Long id) {
        validateCommentExistsById(id);

        var comment = this.getReferenceById(id);

        validateCommentActive(comment);

        return new DtoReturnComment(comment);
    }

    private void validateCommentExistsById(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new CommentNotFoundException(commentId);
        }
    }

    private void validateUserPermission(Comments comment, User user) {
        if (!comment.getUser().equals(user)) {
            throw new ActionNotAllowedException("User is not allowed to perform this action on the comment.");
        }
    }

    private void validateCommentActive(Comments comment) {
        if (!comment.getActive()) {
            throw new CommentDeletedException();
        }
    }

    private void validateCommentInactive(Comments comment) {
        if (comment.getActive()) {
            throw new CommentAlreadyActiveException();
        }
    }

    private void validateUserCanCommentOnPost(User user, Post post) {
        if (!user.getFollowing().contains(post.getPostOwner()) &&
                post.getPostOwner().getProfilePrivacyStatus().equals(PrivacyStatusEnum.PRIVATE)) {
            throw new ActionNotAllowedException("User is not following the post owner.");
        }
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
