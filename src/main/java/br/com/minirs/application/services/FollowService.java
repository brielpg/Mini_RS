package br.com.minirs.application.services;

import br.com.minirs.application.mappers.FollowRequestMapper;
import br.com.minirs.domain.entities.Follow;
import br.com.minirs.domain.entities.FollowRequest;
import br.com.minirs.domain.entities.User;
import br.com.minirs.domain.enums.PrivacyStatusEnum;
import br.com.minirs.domain.exceptions.NotFoundException;
import br.com.minirs.domain.exceptions.UnauthorizedException;
import br.com.minirs.domain.exceptions.follow.InvalidFollowRequestException;
import br.com.minirs.infrastructure.repositories.FollowRepository;
import br.com.minirs.infrastructure.repositories.FollowRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {
    private final UserService userService;
    private final FollowRequestRepository followRequestRepository;
    private final FollowRepository followRepository;
    private final FollowRequestMapper followRequestMapper;

    public FollowService(UserService userService, FollowRequestRepository followRequestRepository, FollowRepository followRepository, FollowRequestMapper followRequestMapper) {
        this.userService = userService;
        this.followRequestRepository = followRequestRepository;
        this.followRepository = followRepository;
        this.followRequestMapper = followRequestMapper;
    }

    @Transactional
    public User acceptFollowRequest(Long requestId, Long requestedUserId) {
        userService.validateUserExistsById(requestedUserId);
        validateFollowRequestExistsById(requestId);

        var followRequest = this.getReferenceById(requestId);
        var requested = userService.getReferenceById(requestedUserId);

        validateRequest(followRequest, requested);

        var follow = new Follow(followRequest.getRequester(), followRequest.getRequested());
        followRepository.save(follow);
        followRequestRepository.delete(followRequest);

        return followRequest.getRequester();
    }

    @Transactional
    public User denyFollowRequest(Long requestId, Long requestedUserId) {
        userService.validateUserExistsById(requestedUserId);
        validateFollowRequestExistsById(requestId);

        var followRequest = this.getReferenceById(requestId);
        var requested = userService.getReferenceById(requestedUserId);

        validateRequest(followRequest, requested);

        followRequestRepository.delete(followRequest);

        return followRequest.getRequester();
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
            var followRequest = followRequestMapper.toEntity(loggedUser, followUser);
            this.save(followRequest);
            return followRequest;
        }

        var follow = new Follow(loggedUser, followUser);
        followRepository.save(follow);

        return loggedUser;
    }

    @Transactional
    public User unfollowUser(Long loggedUserId, Long followUserId) {
        userService.validateUserExistsById(loggedUserId);
        userService.validateUserExistsById(followUserId);

        validateFollowAction(loggedUserId, followUserId, false);

        var loggedUser = userService.getReferenceById(loggedUserId);
        var unfollowUser = userService.getReferenceById(followUserId);

        var follow = followRepository.findByFollowerAndFollowed(loggedUser, unfollowUser)
                .orElseThrow(() -> new InvalidFollowRequestException("User is not following another user."));
        followRepository.delete(follow);

        return loggedUser;
    }

    private void validateFollowRequestExistsById(Long id) {
        if (!followRequestRepository.existsById(id)) {
            throw new NotFoundException("Follow Request", id);
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

        var followOpt = followRepository.findByFollowerAndFollowed(loggedUser, targetUser);

        if (isFollowAction && followOpt.isPresent()) {
            throw new InvalidFollowRequestException("User is already following another user.");
        }

        if (!isFollowAction && followOpt.isEmpty()) {
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
