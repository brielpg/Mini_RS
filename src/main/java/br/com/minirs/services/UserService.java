package br.com.minirs.services;

import br.com.minirs.dto.user.DtoCreateUser;
import br.com.minirs.dto.user.DtoLoginUser;
import br.com.minirs.dto.user.DtoReturnUser;
import br.com.minirs.dto.user.DtoUpdateUser;
import br.com.minirs.entities.User;
import br.com.minirs.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> createUser(DtoCreateUser data) {

        if (this.findByEmail(data.email()) != null) ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Email already registered.");
        if (this.findByUserName(data.userName()) != null) ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: There is already a user with this username.");

        var newUser = new User(data);
        this.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(new DtoReturnUser(newUser));
    }

    @Transactional
    public ResponseEntity<?> loginUser(DtoLoginUser data) {
        var user = this.findByEmail(data.email());

        if (user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ERROR: Invalid Credentials.");
        }

        if (!user.getActive()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("ERROR: User disabled.");
        }

        if (user.getPassword().equals(data.password())){

            return ResponseEntity.status(HttpStatus.CREATED).body(new DtoReturnUser(user));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ERROR: Invalid Credentials.");
    }

    @Transactional
    public ResponseEntity<?> updateUser(DtoUpdateUser data) {
        if (this.existsById(data.id())) {
            var user = this.getReferenceById(data.id());

            if (!user.getActive()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User disabled.");
            }

            if (this.findByEmail(data.email()) != null) ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: Email already registered.");
            if (this.findByUserName(data.userName()) != null) ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: There is already a user with this username.");

            user.updateData(data);
            this.save(user);

            return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnUser(user));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
    }

    @Transactional
    public ResponseEntity<?> deleteUser(Long id) {
        if (this.existsById(id)){
            var user = this.getReferenceById(id);

            if (user.getActive()){
                user.setActive(false);
                this.save(user);

                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnUser(user));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is already disabled.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
    }

    @Transactional
    public ResponseEntity<?> enableUser(Long id) {
        if (this.existsById(id)){
            var user = this.getReferenceById(id);

            if (!user.getActive()){
                user.setActive(true);
                this.save(user);

                return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnUser(user));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is already enabled.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getFollowersList(Long id) {
        if (this.existsById(id)){
            var user = this.getReferenceById(id);

            if (!user.getActive()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("ERROR: User disabled.");
            }

            var followingList = user.getFollowers().stream().map(User::getUserName).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(followingList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getFollowingList(Long id) {
        if (this.existsById(id)){
            var user = this.getReferenceById(id);

            if (!user.getActive()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("ERROR: User disabled.");
            }

            var followingList = user.getFollowing().stream().map(User::getUserName).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(followingList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getAllUsers() {
        List<DtoReturnUser> users = userRepository.findActiveUsers().stream()
                .map(DtoReturnUser::new)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getUserById(Long id) {
        if (this.existsById(id)){
            var user = this.getReferenceById(id);

            if (!user.getActive()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User disabled.");
            }

            return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnUser(user));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: User not found.");
    }

    public Boolean existsById(Long id){
        return userRepository.existsById(id);
    }

    public User getReferenceById(Long id){
        return userRepository.getReferenceById(id);
    }

    @Transactional
    public void save(User user){
        userRepository.save(user);
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }
}
