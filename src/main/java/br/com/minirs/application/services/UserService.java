package br.com.minirs.application.services;

import br.com.minirs.application.dtos.user.UserCreateRequest;
import br.com.minirs.application.dtos.user.UserUpdateRequest;
import br.com.minirs.application.mappers.UserMapper;
import br.com.minirs.domain.entities.Follow;
import br.com.minirs.domain.entities.User;
import br.com.minirs.domain.exceptions.NotFoundException;
import br.com.minirs.domain.exceptions.ResourceDisabledException;
import br.com.minirs.domain.exceptions.user.EmailAlreadyRegisteredException;
import br.com.minirs.domain.exceptions.user.UserNameAlreadyRegisteredException;
import br.com.minirs.infrastructure.repositories.FollowRepository;
import br.com.minirs.infrastructure.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;
    private final FollowRepository followRepository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, FollowRepository followRepository, UserMapper mapper) {
        this.repository = repository;
        this.followRepository = followRepository;
        this.mapper = mapper;
    }

    @Transactional
    public User createUser(UserCreateRequest data) {
        validateEmailAndUsername(data.email(), data.userName());

        User newUser = mapper.toEntity(data);
        this.save(newUser);

        return newUser;
    }

    @Transactional
    public User updateUser(UserUpdateRequest data) {
        validateUserExistsById(data.id());

        User user = this.getReferenceById(data.id());

        validateUserActive(user);
        validateEmailAndUsername(data.email(), data.userName());

        mapper.updateEntity(data, user);
        this.save(user);

        return user;
    }

    @Transactional
    public void disableUser(Long id) {
        validateUserExistsById(id);

        User user = this.getReferenceById(id);

        user.disable();
        this.save(user);
    }

    @Transactional
    public User enableUser(Long id) {
        validateUserExistsById(id);

        User user = this.getReferenceById(id);

        user.enable();
        this.save(user);

        return user;
    }

    @Transactional(readOnly = true)
    public List<User> getFollowersList(Long id) {
        validateUserExistsById(id);

        User user = this.getReferenceById(id);

        validateUserActive(user);

        return followRepository.findByFollowed(user).stream()
                .map(Follow::getFollower)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<User> getFollowingList(Long id) {
        validateUserExistsById(id);

        User user = this.getReferenceById(id);

        validateUserActive(user);

        return followRepository.findByFollower(user).stream()
                .map(Follow::getFollowed)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return repository.findActiveUsers();
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        validateUserExistsById(id);

        User user = this.getReferenceById(id);

        this.validateUserActive(user);

        return user;
    }

    public void validateUserExistsById(Long id) {
        if (!repository.existsById(id)) {
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
        if (!user.isActive()) {
            throw new ResourceDisabledException("User is already deactivated");
        }
    }

    @Transactional
    public void save(User user) {
        repository.save(user);
    }

    @Transactional(readOnly = true)
    private User findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    private User findByUserName(String username) {
        return repository.findByUserName(username);
    }

    @Transactional(readOnly = true)
    public User getReferenceById(Long id) {
        return repository.getReferenceById(id);
    }
}
