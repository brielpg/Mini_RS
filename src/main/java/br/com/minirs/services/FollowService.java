package br.com.minirs.services;

import br.com.minirs.dto.follow.DtoReturnFollowRequest;
import br.com.minirs.dto.user.DtoReturnUser;
import br.com.minirs.entities.FollowRequest;
import br.com.minirs.entities.User;
import br.com.minirs.enums.PrivacyStatusEnum;
import br.com.minirs.exceptions.NotFoundException;
import br.com.minirs.exceptions.ResourceDisabledException;
import br.com.minirs.exceptions.UnauthorizedException;
import br.com.minirs.exceptions.follow.InvalidFollowRequestException;
import br.com.minirs.repositories.FollowRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    @Autowired
    private UserService userService;

    @Autowired
    private FollowRequestRepository followRequestRepository;

    @Transactional
    public DtoReturnUser acceptFollowRequest(Long requestId, Long requestedUserId) {
        userService.validateUserExistsById(requestedUserId);
        validateFollowRequestExistsById(requestId);

        var followRequest = this.getReferenceById(requestId);
        var requested = userService.getReferenceById(requestedUserId);

        validateFollowRequestActive(followRequest);
        validateRequest(followRequest, requested);

        followRequest.acceptRequest();
        this.save(followRequest);

        return new DtoReturnUser(followRequest.getRequester());
    }

    @Transactional
    public DtoReturnUser denyFollowRequest(Long requestId, Long requestedUserId) {
        userService.validateUserExistsById(requestedUserId);
        validateFollowRequestExistsById(requestId);

        var followRequest = this.getReferenceById(requestId);
        var requested = userService.getReferenceById(requestedUserId);

        validateFollowRequestActive(followRequest);
        validateRequest(followRequest, requested);

        followRequest.denyRequest();
        this.save(followRequest);

        return new DtoReturnUser(followRequest.getRequester());
    }

    @Transactional
    public Object followUser(Long loggedUserId, Long followUserId) {
        userService.validateUserExistsById(loggedUserId);
        userService.validateUserExistsById(followUserId);

        validateFollowAction(loggedUserId, followUserId, true);

        var loggedUser = userService.getReferenceById(loggedUserId);
        var followUser = userService.getReferenceById(followUserId);

        if (followUser.getProfilePrivacyStatus().equals(PrivacyStatusEnum.PRIVATE)) {
            if (followRequestRepository.existsActiveFollowRequest(loggedUser.getId(), followUser.getId())) {
                throw new InvalidFollowRequestException("A follow request has already been submitted.");
            }
            var followRequest = new FollowRequest(loggedUser, followUser);
            this.save(followRequest);
            return new DtoReturnFollowRequest(followRequest);
        }

        loggedUser.followUser(followUser);
        userService.save(loggedUser);

        return new DtoReturnUser(loggedUser);
    }

    @Transactional
    public DtoReturnUser unfollowUser(Long loggedUserId, Long followUserId) {
        userService.validateUserExistsById(loggedUserId);
        userService.validateUserExistsById(followUserId);

        validateFollowAction(loggedUserId, followUserId, false);

        var loggedUser = userService.getReferenceById(loggedUserId);
        var unfollowUser = userService.getReferenceById(followUserId);

        loggedUser.unfollowUser(unfollowUser);
        userService.save(loggedUser);

        return new DtoReturnUser(loggedUser);
    }

    private void validateFollowRequestExistsById(Long id) {
        if (!followRequestRepository.existsById(id)) {
            throw new NotFoundException("Follow Request", id);
        }
    }

    private void validateFollowRequestActive(FollowRequest followRequest) {
        if (!followRequest.getActive()) {
            throw new ResourceDisabledException("Follow Request", followRequest.getId());
        }
    }

    private void validateRequest(FollowRequest followRequest, User requested) {
        if (!followRequest.getRequested().equals(requested)) {
            throw new InvalidFollowRequestException("User cannot process the follow request.");
        }
    }

    private void validateFollowAction(Long loggedUserId, Long targetUserId, boolean isFollowAction) {
        if (loggedUserId.equals(targetUserId)) {
            throw new UnauthorizedException("User can't perform action on himself.");
        }

        var loggedUser = userService.getReferenceById(loggedUserId);
        var targetUser = userService.getReferenceById(targetUserId);

        userService.validateUserActive(loggedUser);
        userService.validateUserActive(targetUser);

        if (isFollowAction && loggedUser.getFollowing().contains(targetUser)) {
            throw new InvalidFollowRequestException("User is already following another user.");
        }

        if (!isFollowAction && !loggedUser.getFollowing().contains(targetUser)) {
            throw new InvalidFollowRequestException("User is not following another user.");
        }
    }

    private FollowRequest getReferenceById(Long id) {
        return followRequestRepository.getReferenceById(id);
    }

    @Transactional
    private void save(FollowRequest followRequest) {
        followRequestRepository.save(followRequest);
    }
}
