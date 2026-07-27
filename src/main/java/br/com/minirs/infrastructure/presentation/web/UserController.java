package br.com.minirs.infrastructure.presentation.web;

import br.com.minirs.application.dtos.user.UserCreateRequest;
import br.com.minirs.application.dtos.user.UserResponse;
import br.com.minirs.application.dtos.user.UserUpdateRequest;
import br.com.minirs.application.mappers.UserMapper;
import br.com.minirs.application.services.UserService;
import br.com.minirs.domain.entities.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users.stream().map(userMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @GetMapping("/followers/{id}")
    public ResponseEntity<List<UserResponse>> getFollowersList(@PathVariable Long id){
        List<User> followers = userService.getFollowersList(id);
        return ResponseEntity.ok(followers.stream().map(userMapper::toResponse).toList());
    }

    @GetMapping("/following/{id}")
    public ResponseEntity<List<UserResponse>> getFollowingList(@PathVariable Long id){
        List<User> following = userService.getFollowingList(id);
        return ResponseEntity.ok(following.stream().map(userMapper::toResponse).toList());
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserCreateRequest data) {
        User user = userService.createUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toResponse(user));
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserUpdateRequest data){
        User user = userService.updateUser(data);
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @DeleteMapping("/disable/{id}")
    public ResponseEntity<Void> disableUser(@PathVariable Long id){
        userService.disableUser(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/enable/{id}")
    public ResponseEntity<UserResponse> enableUser(@PathVariable Long id){
        User user = userService.enableUser(id);
        return ResponseEntity.ok(userMapper.toResponse(user));
    }
}
