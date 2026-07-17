package br.com.minirs.domain.entities;

import br.com.minirs.application.dtos.post.PostCreateRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "minirs_posts")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode(of = "id")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Boolean active;
    private LocalDate publishDate;
    private List<LocalDate> updateDates = new ArrayList<>();
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private User postOwner;
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Comments> comment = new HashSet<>();
    @JsonIgnore
    private List<Long> likesByUserId = new ArrayList<>();
    private Integer likeCount;

    public Post(PostCreateRequest data, User postOwner){
        this.content = data.content();
        this.active = true;
        this.publishDate = LocalDate.now();
        this.likeCount = 0;
        this.postOwner = postOwner;
    }

    public void updatePost(String content) {
        this.content = content;
        this.updateDates.add(LocalDate.now());
    }
}
