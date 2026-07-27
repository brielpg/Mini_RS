package br.com.minirs.application.mappers;

import br.com.minirs.application.dtos.follow.FollowRequestResponse;
import br.com.minirs.domain.entities.FollowRequest;
import br.com.minirs.domain.entities.User;
import org.springframework.stereotype.Component;

@Component
public class FollowRequestMapper {
    private final UserMapper userMapper;

    public FollowRequestMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public FollowRequest toEntity(User requester, User requested) {
        return new FollowRequest(requester, requested);
    }

    public FollowRequestResponse toResponse(FollowRequest followRequest) {
        return new FollowRequestResponse(
                followRequest.getId(),
                userMapper.toResponse(followRequest.getRequester()),
                userMapper.toResponse(followRequest.getRequested()),
                followRequest.getRequestedAt()
        );
    }
}
