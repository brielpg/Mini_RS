package br.com.minirs.application.services;

import br.com.minirs.application.dtos.user.UserCreateRequest;
import br.com.minirs.application.dtos.user.UserResponse;
import br.com.minirs.application.dtos.user.UserUpdateRequest;
import br.com.minirs.domain.entities.User;
import br.com.minirs.domain.exceptions.NotFoundException;
import br.com.minirs.domain.exceptions.ResourceAlreadyActiveException;
import br.com.minirs.domain.exceptions.ResourceDisabledException;
import br.com.minirs.domain.exceptions.user.EmailAlreadyRegisteredException;
import br.com.minirs.domain.exceptions.user.UserNameAlreadyRegisteredException;
import br.com.minirs.infrastructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserResponse createUser(UserCreateRequest data) {
        validateEmailAndUsername(data.email(), data.userName());

        var newUser = new User(data);
        this.save(newUser);

        return new UserResponse(newUser);
    }

    @Transactional
    public UserResponse updateUser(UserUpdateRequest data) {
        validateUserExistsById(data.id());

        var user = this.getReferenceById(data.id());

        validateUserActive(user);
        validateEmailAndUsername(data.email(), data.userName());

        user.updateData(data);
        this.save(user);

        return new UserResponse(user);
    }

    @Transactional
    public UserResponse disableUser(Long id) {
        validateUserExistsById(id);

        var user = this.getReferenceById(id);

        if (!user.getActive()) throw new ResourceDisabledException("User is already disabled");

        user.setActive(false);
        this.save(user);

        return new UserResponse(user);

    }

    @Transactional
    public UserResponse enableUser(Long id) {
        validateUserExistsById(id);

        var user = this.getReferenceById(id);

        if (user.getActive()) throw new ResourceAlreadyActiveException("User", id);

        user.setActive(true);
        this.save(user);

        return new UserResponse(user);
    }

    @Transactional(readOnly = true)
    public List<String> getFollowersList(Long id) {
        validateUserExistsById(id);

        var user = this.getReferenceById(id);

        validateUserActive(user);

        return user.getFollowers().stream()
                .map(User::getUserName)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<String> getFollowingList(Long id) {
        validateUserExistsById(id);

        var user = this.getReferenceById(id);

        validateUserActive(user);

        return user.getFollowing().stream()
                .map(User::getUserName)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findActiveUsers().stream()
                .map(UserResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        validateUserExistsById(id);

        var user = this.getReferenceById(id);

        this.validateUserActive(user);

        return new UserResponse(user);
    }

    public void validateUserExistsById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User", id);
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

    public void validateUserActive(User user) {
        if (!user.getActive()) {
            throw new ResourceDisabledException("User", user.getId());
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

    public User getReferenceById(Long id) {
        return userRepository.getReferenceById(id);
    }
}
