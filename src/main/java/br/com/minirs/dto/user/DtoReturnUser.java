package br.com.minirs.dto.user;

import br.com.minirs.entities.PrivacyStatusEnum;
import br.com.minirs.entities.User;

public class DtoReturnUser {
    public Long id;
    public String fullName;
    public String userName;
    public String email;
    public PrivacyStatusEnum profilePrivacyStatus;
    public Integer followersCount;
    public Integer followingCount;
    public Integer postCount;

    public DtoReturnUser(User user){
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.profilePrivacyStatus = user.getProfilePrivacyStatus();
        this.followersCount = user.getFollowersCount();
        this.followingCount = user.getFollowingCount();
        this.postCount = user.getPostCount();
    }
}
