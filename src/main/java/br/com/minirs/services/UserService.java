package br.com.minirs.services;

import br.com.minirs.dto.user.DtoCreateUser;
import br.com.minirs.dto.user.DtoReturnUser;
import br.com.minirs.dto.user.DtoUpdateUser;
import br.com.minirs.entities.User;
import br.com.minirs.exceptions.NotFoundException;
import br.com.minirs.exceptions.ResourceAlreadyActiveException;
import br.com.minirs.exceptions.ResourceDisabledException;
import br.com.minirs.exceptions.user.EmailAlreadyRegisteredException;
import br.com.minirs.exceptions.user.UserNameAlreadyRegisteredException;
import br.com.minirs.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public DtoReturnUser createUser(DtoCreateUser data) {
        validateEmailAndUsername(data.email(), data.userName());

        var newUser = new User(data);
        this.save(newUser);

        return new DtoReturnUser(newUser);
    }

    @Transactional
    public DtoReturnUser updateUser(DtoUpdateUser data) {
        validateUserExistsById(data.id());

        var user = this.getReferenceById(data.id());

        validateUserActive(user);
        validateEmailAndUsername(data.email(), data.userName());

        user.updateData(data);
        this.save(user);

        return new DtoReturnUser(user);
    }

    @Transactional
    public DtoReturnUser disableUser(Long id) {
        validateUserExistsById(id);

        var user = this.getReferenceById(id);

        if (!user.getActive()) throw new ResourceDisabledException("User is already disabled");

        user.setActive(false);
        this.save(user);

        return new DtoReturnUser(user);

    }

    @Transactional
    public DtoReturnUser enableUser(Long id) {
        validateUserExistsById(id);

        var user = this.getReferenceById(id);

        if (user.getActive()) throw new ResourceAlreadyActiveException("User", id);

        user.setActive(true);
        this.save(user);

        return new DtoReturnUser(user);
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
    public List<DtoReturnUser> getAllUsers() {
        return userRepository.findActiveUsers().stream()
                .map(DtoReturnUser::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public DtoReturnUser getUserById(Long id) {
        validateUserExistsById(id);

        var user = this.getReferenceById(id);

        this.validateUserActive(user);

        return new DtoReturnUser(user);
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
