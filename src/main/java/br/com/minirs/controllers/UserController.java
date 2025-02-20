package br.com.minirs.controllers;

import br.com.minirs.dto.user.DtoCreateUser;
import br.com.minirs.dto.user.DtoLoginUser;
import br.com.minirs.dto.user.DtoUpdateUser;
import br.com.minirs.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/followers/{id}")
    public ResponseEntity<?> getFollowersList(@PathVariable Long id){
        return userService.getFollowersList(id);
    }

    @GetMapping("/following/{id}")
    public ResponseEntity<?> getFollowingList(@PathVariable Long id){
        return userService.getFollowingList(id);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> createUser(@RequestBody @Valid DtoCreateUser data) {
        return userService.createUser(data);
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<?> loginUser(@RequestBody @Valid DtoLoginUser data){
        return userService.loginUser(data);
    }

    @PostMapping("/follow/{loggedUserId}/{followUserId}")
    @Transactional
    public ResponseEntity<?> followUser(@PathVariable Long loggedUserId, @PathVariable Long followUserId){
        return userService.followUser(loggedUserId, followUserId);
    }

    @PostMapping("/unfollow/{loggedUserId}/{followUserId}")
    @Transactional
    public ResponseEntity<?> unfollowUser(@PathVariable Long loggedUserId, @PathVariable Long followUserId){
        return userService.unfollowUser(loggedUserId, followUserId);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<?> updateUser(@RequestBody @Valid DtoUpdateUser data){
        return userService.updateUser(data);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }

    @DeleteMapping("/enable/{id}")
    @Transactional
    public ResponseEntity<?> enableUser(@PathVariable Long id){
        return userService.enableUser(id);
    }
}
