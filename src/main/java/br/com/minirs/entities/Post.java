package br.com.minirs.entities;

import br.com.minirs.dto.post.DtoCreatePost;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private Integer likeCount;

    public Post(DtoCreatePost data, User postOwner){
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
