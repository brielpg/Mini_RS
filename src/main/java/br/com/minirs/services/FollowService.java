package br.com.minirs.services;

import br.com.minirs.dto.follow.DtoReturnFollowRequest;
import br.com.minirs.dto.user.DtoReturnUser;
import br.com.minirs.entities.FollowRequest;
import br.com.minirs.entities.PrivacyStatusEnum;
import br.com.minirs.repositories.FollowRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    @Autowired
    private UserService userService;

    @Autowired
    private FollowRequestRepository followRequestRepository;

    @Transactional
    public ResponseEntity<?> acceptFollowRequest(Long requestId, Long requestedUserId) {
        if (this.existsById(requestId) && userService.existsById(requestedUserId)){
            var followRequest = this.getReferenceById(requestId);
            var requested = userService.getReferenceById(requestedUserId);

            if(followRequest.getRequested().equals(requested) && followRequest.getActive()){
                followRequest.acceptRequest();
                this.save(followRequest);

                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnUser(followRequest.getRequester()));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User cannot accept the follow request.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User or Follow Request not found.");
    }

    @Transactional
    public ResponseEntity<?> denyFollowRequest(Long requestId, Long requestedUserId) {
        if (this.existsById(requestId) && userService.existsById(requestedUserId)){
            var followRequest = this.getReferenceById(requestId);
            var requested = userService.getReferenceById(requestedUserId);

            if(followRequest.getRequested().equals(requested) && followRequest.getActive()){
                followRequest.denyRequest();
                this.save(followRequest);

                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnUser(followRequest.getRequester()));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User cannot deny the follow request.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User or Follow Request not found.");
    }

    @Transactional
    public ResponseEntity<?> followUser(Long loggedUserId, Long followUserId) {
        if (userService.existsById(loggedUserId) && userService.existsById(followUserId)){

            if (loggedUserId.equals(followUserId)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User can't follow himself");
            }

            var loggedUser = userService.getReferenceById(loggedUserId);
            var followUser = userService.getReferenceById(followUserId);

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
                this.save(followRequest);
                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnFollowRequest(followRequest));
            }

            loggedUser.followUser(followUser);
            userService.save(loggedUser);

            return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnUser(loggedUser));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
    }

    @Transactional
    public ResponseEntity<?> unfollowUser(Long loggedUserId, Long followUserId) {
        if (userService.existsById(loggedUserId) && userService.existsById(followUserId)){

            if (loggedUserId.equals(followUserId)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User can't unfollow himself");
            }

            var loggedUser = userService.getReferenceById(loggedUserId);
            var unfollowUser = userService.getReferenceById(followUserId);

            if (!loggedUser.getFollowing().contains(unfollowUser)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is not following another user.");
            }

            if (!loggedUser.getActive() || !unfollowUser.getActive()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: One of the users is inactive.");
            }

            loggedUser.unfollowUser(unfollowUser);
            userService.save(loggedUser);

            return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnUser(loggedUser));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
    }

    private boolean existsById(Long id) {
        return followRequestRepository.existsById(id);
    }

    private FollowRequest getReferenceById(Long id){
        return followRequestRepository.getReferenceById(id);
    }

    @Transactional
    private void save(FollowRequest followRequest){
        followRequestRepository.save(followRequest);
    }
}
