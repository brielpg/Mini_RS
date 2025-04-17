package br.com.minirs.services;

import br.com.minirs.dto.user.DtoCreateUser;
import br.com.minirs.dto.user.DtoLoginUser;
import br.com.minirs.dto.user.DtoReturnUser;
import br.com.minirs.dto.user.DtoUpdateUser;
import br.com.minirs.entities.User;
import br.com.minirs.exceptions.user.EmailAlreadyRegisteredException;
import br.com.minirs.exceptions.user.UserDisabledException;
import br.com.minirs.exceptions.user.UserNameAlreadyRegisteredException;
import br.com.minirs.exceptions.user.UserNotFoundException;
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
        validateEmailAndUsername(data.email(), data.userName());

        var newUser = new User(data);
        this.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(new DtoReturnUser(newUser));
    }

    @Transactional
    public ResponseEntity<?> updateUser(DtoUpdateUser data) {
        validateUserExistsById(data.id());

        var user = this.getReferenceById(data.id());

        validateUserActive(user);
        validateEmailAndUsername(data.email(), data.userName());

        user.updateData(data);
        this.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnUser(user));
    }

    @Transactional
    public ResponseEntity<?> deleteUser(Long id) {
        validateUserExistsById(id);

        var user = this.getReferenceById(id);

        if (user.getActive()) {
            user.setActive(false);
            this.save(user);

            return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnUser(user));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is already disabled.");
    }

    @Transactional
    public ResponseEntity<?> enableUser(Long id) {
        validateUserExistsById(id);

        var user = this.getReferenceById(id);

        if (!user.getActive()) {
            user.setActive(true);
            this.save(user);

            return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnUser(user));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is already enabled.");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getFollowersList(Long id) {
        validateUserExistsById(id);

        var user = this.getReferenceById(id);

        validateUserActive(user);

        var followingList = user.getFollowers().stream()
                .map(User::getUserName)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(followingList);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getFollowingList(Long id) {
        validateUserExistsById(id);

        var user = this.getReferenceById(id);

        validateUserActive(user);

        var followingList = user.getFollowing().stream()
                .map(User::getUserName)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(followingList);
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
        validateUserExistsById(id);

        var user = this.getReferenceById(id);

        this.validateUserActive(user);

        return ResponseEntity.status(HttpStatus.OK).body(new DtoReturnUser(user));
    }

    public void validateUserExistsById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
    }

    private void validateEmailAndUsername(String email, String username) {
        if (this.findByEmail(email) != null) {
            throw new EmailAlreadyRegisteredException();
        }

        if (this.findByUserName(username) != null) {
            throw new UserNameAlreadyRegisteredException();
        }
    }

    private void validateUserActive(User user) {
        if (!user.getActive()) {
            throw new UserDisabledException();
        }
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    public Boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public User getReferenceById(Long id) {
        return userRepository.getReferenceById(id);
    }
}
