package br.com.minirs.controllers;

import br.com.minirs.dto.user.DtoReturnUser;
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
        var user = followService.followUser(loggedUserId, followUserId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/unfollow/{loggedUserId}/{followUserId}")
    public ResponseEntity<DtoReturnUser> unfollowUser(@PathVariable Long loggedUserId, @PathVariable Long followUserId){
        var user = followService.unfollowUser(loggedUserId, followUserId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/accept/{requestId}/{requestedUserId}")
    public ResponseEntity<DtoReturnUser> acceptFollowRequest(@PathVariable Long requestId, @PathVariable Long requestedUserId){
        var user = followService.acceptFollowRequest(requestId, requestedUserId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/deny/{requestId}/{requestedUserId}")
    public ResponseEntity<DtoReturnUser> denyFollowRequest(@PathVariable Long requestId, @PathVariable Long requestedUserId){
        var user = followService.denyFollowRequest(requestId, requestedUserId);
        return ResponseEntity.ok(user);
    }
}
