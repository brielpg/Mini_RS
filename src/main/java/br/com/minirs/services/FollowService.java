package br.com.minirs.services;

import br.com.minirs.dto.follow.DtoReturnFollowRequest;
import br.com.minirs.dto.user.DtoReturnUser;
import br.com.minirs.entities.FollowRequest;
import br.com.minirs.entities.PrivacyStatusEnum;
import br.com.minirs.repositories.FollowRequestRepository;
import br.com.minirs.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRequestRepository followRequestRepository;

    @Transactional
    public ResponseEntity<?> acceptFollowRequest(Long requestId, Long requestedUserId) {
        if (followRequestRepository.existsById(requestId) && userRepository.existsById(requestedUserId)){
            var followRequest = followRequestRepository.getReferenceById(requestId);
            var requested = userRepository.getReferenceById(requestedUserId);

            if(followRequest.getRequested().equals(requested) && followRequest.getActive()){
                followRequest.acceptRequest();

                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnUser(followRequest.getRequester()));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User cannot accept the follow request.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User or Follow Request not found.");
    }

    @Transactional
    public ResponseEntity<?> denyFollowRequest(Long requestId, Long requestedUserId) {
        if (followRequestRepository.existsById(requestId) && userRepository.existsById(requestedUserId)){
            var followRequest = followRequestRepository.getReferenceById(requestId);
            var requested = userRepository.getReferenceById(requestedUserId);

            if(followRequest.getRequested().equals(requested) && followRequest.getActive()){
                followRequest.denyRequest();

                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnUser(followRequest.getRequester()));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User cannot deny the follow request.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User or Follow Request not found.");
    }

    @Transactional
    public ResponseEntity<?> followUser(Long loggedUserId, Long followUserId) {
        if (userRepository.existsById(loggedUserId) && userRepository.existsById(followUserId)){

            if (loggedUserId.equals(followUserId)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User can't follow himself");
            }

            var loggedUser = userRepository.getReferenceById(loggedUserId);
            var followUser = userRepository.getReferenceById(followUserId);

            if (loggedUser.getFollowing().contains(followUser)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is already following another user.");
            }

            if (!loggedUser.getActive() || !followUser.getActive()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: One of the users is inactive.");
            }

            if (followUser.getProfilePrivacyStatus().equals(PrivacyStatusEnum.PRIVATE)){
                if (followRequestRepository.existsActiveFollowRequest(loggedUser.getId(), followUser.getId())){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: A follow request has already been submitted.");
                }
                var followRequest = new FollowRequest(loggedUser, followUser);
                followRequestRepository.save(followRequest);
                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnFollowRequest(followRequest));
            }

            loggedUser.followUser(followUser);

            return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnUser(loggedUser));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
    }

    @Transactional
    public ResponseEntity<?> unfollowUser(Long loggedUserId, Long followUserId) {
        if (userRepository.existsById(loggedUserId) && userRepository.existsById(followUserId)){

            if (loggedUserId.equals(followUserId)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User can't unfollow himself");
            }

            var loggedUser = userRepository.getReferenceById(loggedUserId);
            var unfollowUser = userRepository.getReferenceById(followUserId);

            if (!loggedUser.getFollowing().contains(unfollowUser)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is not following another user.");
            }

            if (!loggedUser.getActive() || !unfollowUser.getActive()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: One of the users is inactive.");
            }

            loggedUser.unfollowUser(unfollowUser);

            return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnUser(loggedUser));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
    }
}
