package br.com.minirs.entities;

import br.com.minirs.dto.user.DtoCreateUser;
import br.com.minirs.dto.user.DtoUpdateUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "minirs_users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode(of = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //USER INFO
    private String fullName;
    private String userName;
    private String email;
    private LocalDate birthDate;
    private LocalDate registrationDate;
    private String biography;
    private String gender;
    @JsonIgnore
    private String password;

    //STATUS
    private Boolean active;
    @Enumerated(EnumType.STRING)
    private PrivacyStatusEnum profilePrivacyStatus;
    private Integer followersCount;
    private Integer followingCount;
    private Integer postCount;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "followers",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "followerId")
    )
    private Set<User> followers = new HashSet<>();

    @ManyToMany(mappedBy = "followers", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<User> following = new HashSet<>();

    public User(DtoCreateUser data){
        this.fullName = data.fullName();
        this.userName = data.userName();
        this.email = data.email();
        this.birthDate = data.birthDate();
        this.password = data.password();
        this.profilePrivacyStatus = data.profilePrivacyStatus();

        this.registrationDate = LocalDate.now();
        this.active = true;
        this.followersCount = 0;
        this.followingCount = 0;
        this.postCount = 0;

        if (data.biography() != null){ this.biography = data.biography(); }
        if (data.gender() != null){ this.gender = data.gender(); }
    }

    public void updateData(DtoUpdateUser data) {
        if (data.fullName() != null){
            this.fullName = data.fullName();
        }
        if (data.userName() != null){
            this.userName = data.userName();
        }
        if (data.email() != null){
            this.email = data.email();
        }
        if (data.birthDate() != null){
            this.birthDate = data.birthDate();
        }
        if(data.profilePrivacyStatus() != null){
            this.profilePrivacyStatus = data.profilePrivacyStatus();
        }
        if (data.password() != null){
            this.password = data.password();
        }
        if (data.biography() != null){
            this.biography = data.biography();
        }
        if (data.gender() != null){
            this.gender = data.gender();
        }
    }

    public void followUser(User userToFollow) {
        if (!this.following.contains(userToFollow)) {
            this.following.add(userToFollow);
            userToFollow.getFollowers().add(this);

            userToFollow.setFollowersCount(userToFollow.getFollowersCount()+1);
            this.followingCount++;
        }
    }

    public void unfollowUser(User userToUnfollow) {
        this.following.remove(userToUnfollow);
        userToUnfollow.getFollowers().remove(this);

        userToUnfollow.setFollowersCount(userToUnfollow.getFollowersCount()-1);
        this.followingCount--;
    }
}
