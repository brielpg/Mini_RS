package br.com.minirs.application.mappers;

import br.com.minirs.application.dtos.user.UserCreateRequest;
import br.com.minirs.application.dtos.user.UserResponse;
import br.com.minirs.application.dtos.user.UserUpdateRequest;
import br.com.minirs.domain.entities.User;
import br.com.minirs.domain.valueobjects.Email;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public User toEntity(UserCreateRequest request) {
        return new User(
                request.fullName(),
                request.userName(),
                new Email(request.email()),
                request.birthDate(),
                request.password()
        );
    }

    public void updateEntity(UserUpdateRequest request, User user) {
        if (request.fullName() != null) {
            user.changeFullName(request.fullName());
        }
        if (request.userName() != null) {
            user.changeUserName(request.userName());
        }
        if (request.email() != null) {
            user.changeEmail(new Email(request.email()));
        }
        if (request.birthDate() != null && request.birthDate().isBefore(LocalDateTime.now())) {
            user.changeBirthDate(request.birthDate());
        }
        if (request.password() != null) {
            user.changePassword(request.password());
        }
        if (request.biography() != null) {
            user.changeBiography(request.biography());
        }
        if (request.profilePrivacyStatus() != null) {
            user.changeProfilePrivacyStatus(request.profilePrivacyStatus());
        }
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getUserName(),
                user.getEmail(),
                user.getBirthDate(),
                user.getProfilePrivacyStatus(),
                0,
                0,
                0,
                user.getCreatedAt()
        );
    }
}
