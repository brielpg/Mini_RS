package br.com.minirs.services;

import br.com.minirs.dto.user.DtoCreateUser;
import br.com.minirs.dto.user.DtoLoginUser;
import br.com.minirs.dto.user.DtoReturnUser;
import br.com.minirs.dto.user.DtoUpdateUser;
import br.com.minirs.models.User;
import br.com.minirs.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> createUser(DtoCreateUser data) {
        if (userRepository.findByEmail(data.email()) != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Email already registered.");
        }

        if (userRepository.findByUserName(data.userName()) != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: There is already a user with this username.");
        }

        var newUser = new User(data);
        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(new DtoReturnUser(newUser));
    }

    @Transactional
    public ResponseEntity<?> loginUser(DtoLoginUser data) {
        var user = userRepository.findByEmail(data.email());
        if (user != null && user.getPassword().equals(data.password())){

            return ResponseEntity.status(HttpStatus.CREATED).body(new DtoReturnUser(user));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ERROR: Email or Password incorrect.");
    }

    @Transactional
    public ResponseEntity<?> updateUser(DtoUpdateUser data) {
        if (userRepository.existsById(data.id())) {
            var user = userRepository.getReferenceById(data.id());

            if (!user.getActive()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User disabled.");
            }

            if (userRepository.findByEmail(data.email()) != null){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Email already registered.");
            }
            if (userRepository.findByUserName(data.userName()) != null){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: There is already a user with this username.");
            }

            user.updateData(data);

            return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnUser(user));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
    }

    @Transactional
    public ResponseEntity<?> deleteUser(Long id) {
        if (userRepository.existsById(id)){
            var user = userRepository.getReferenceById(id);
            if (user.getActive()){
                user.setActive(false);

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DtoReturnUser(user));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is already disabled.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
    }

    @Transactional
    public ResponseEntity<?> enableUser(Long id) {
        if (userRepository.existsById(id)){
            var user = userRepository.getReferenceById(id);
            if (!user.getActive()){
                user.setActive(true);

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DtoReturnUser(user));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is already enabled.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
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

    @Transactional
    public ResponseEntity<?> getFollowersList(Long id) {
        if (userRepository.existsById(id)){
            var user = userRepository.getReferenceById(id);
            var followingList = user.getFollowers().stream().map(User::getUserName).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(followingList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
    }

    @Transactional
    public ResponseEntity<?> getFollowingList(Long id) {
        if (userRepository.existsById(id)){
            var user = userRepository.getReferenceById(id);
            var followingList = user.getFollowing().stream().map(User::getUserName).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(followingList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
    }
}
