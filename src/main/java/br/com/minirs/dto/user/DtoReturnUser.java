package br.com.minirs.dto.user;

import br.com.minirs.models.User;

import java.time.LocalDate;

public class DtoReturnUser {
    public Long id;
    public String fullName;
    public String userName;
    public String email;
    public Integer followersCount;
    public Integer followingCount;

    public DtoReturnUser(User user){
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.followersCount = user.getFollowersCount();
        this.followingCount = user.getFollowingCount();
    }
}
