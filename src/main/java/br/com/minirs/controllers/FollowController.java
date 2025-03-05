package br.com.minirs.controllers;

import br.com.minirs.services.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/{loggedUserId}/{followUserId}")
    public ResponseEntity<?> followUser(@PathVariable Long loggedUserId, @PathVariable Long followUserId){
        return followService.followUser(loggedUserId, followUserId);
    }

    @PostMapping("/unfollow/{loggedUserId}/{followUserId}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long loggedUserId, @PathVariable Long followUserId){
        return followService.unfollowUser(loggedUserId, followUserId);
    }

    @PostMapping("/accept/{requestId}/{requestedUserId}")
    public ResponseEntity<?> acceptFollowRequest(@PathVariable Long requestId, @PathVariable Long requestedUserId){
        return followService.acceptFollowRequest(requestId, requestedUserId);
    }

    @PostMapping("/deny/{requestId}/{requestedUserId}")
    public ResponseEntity<?> denyFollowRequest(@PathVariable Long requestId, @PathVariable Long requestedUserId){
        return followService.denyFollowRequest(requestId, requestedUserId);
    }
}
