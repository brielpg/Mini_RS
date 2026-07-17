package br.com.minirs.infrastructure.presentation.web;

import br.com.minirs.application.dtos.user.UserCreateRequest;
import br.com.minirs.application.dtos.user.UserResponse;
import br.com.minirs.application.dtos.user.UserUpdateRequest;
import br.com.minirs.application.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        var users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        var user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/followers/{id}")
    public ResponseEntity<List<String>> getFollowersList(@PathVariable Long id){
        var followers = userService.getFollowersList(id);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/following/{id}")
    public ResponseEntity<List<String>> getFollowingList(@PathVariable Long id){
        var following = userService.getFollowingList(id);
        return ResponseEntity.ok(following);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserCreateRequest data) {
        var user = userService.createUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserUpdateRequest data){
        var user = userService.updateUser(data);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/disable/{id}")
    public ResponseEntity<UserResponse> disableUser(@PathVariable Long id){
        var user = userService.disableUser(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/enable/{id}")
    public ResponseEntity<?> enableUser(@PathVariable Long id){
        var user = userService.enableUser(id);
        return ResponseEntity.ok(user);
    }
}
