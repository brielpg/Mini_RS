package br.com.minirs.infrastructure.presentation.web;

import br.com.minirs.application.dtos.follow.FollowRequestResponse;
import br.com.minirs.application.dtos.user.UserResponse;
import br.com.minirs.application.mappers.FollowRequestMapper;
import br.com.minirs.application.mappers.UserMapper;
import br.com.minirs.application.services.FollowService;
import br.com.minirs.domain.entities.FollowRequest;
import br.com.minirs.domain.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/follow")
public class FollowController {
    private final FollowService followService;
    private final UserMapper userMapper;
    private final FollowRequestMapper followRequestMapper;

    public FollowController(FollowService followService, UserMapper userMapper, FollowRequestMapper followRequestMapper) {
        this.followService = followService;
        this.userMapper = userMapper;
        this.followRequestMapper = followRequestMapper;
    }

    @PostMapping("/{loggedUserId}/{followUserId}")
    public ResponseEntity<?> followUser(@PathVariable Long loggedUserId, @PathVariable Long followUserId){
        Object result = followService.followUser(loggedUserId, followUserId);
        if (result instanceof User user) {
            return ResponseEntity.ok(userMapper.toResponse(user));
        }
        if (result instanceof FollowRequest followRequest) {
            return ResponseEntity.ok(followRequestMapper.toResponse(followRequest));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/unfollow/{loggedUserId}/{followUserId}")
    public ResponseEntity<UserResponse> unfollowUser(@PathVariable Long loggedUserId, @PathVariable Long followUserId){
        User user = followService.unfollowUser(loggedUserId, followUserId);
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @PostMapping("/accept/{requestId}/{requestedUserId}")
    public ResponseEntity<UserResponse> acceptFollowRequest(@PathVariable Long requestId, @PathVariable Long requestedUserId){
        User user = followService.acceptFollowRequest(requestId, requestedUserId);
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @PostMapping("/deny/{requestId}/{requestedUserId}")
    public ResponseEntity<UserResponse> denyFollowRequest(@PathVariable Long requestId, @PathVariable Long requestedUserId){
        User user = followService.denyFollowRequest(requestId, requestedUserId);
        return ResponseEntity.ok(userMapper.toResponse(user));
    }
}
