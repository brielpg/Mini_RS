package br.com.minirs.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "minirs_comments")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode(of = "id")
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Boolean active;
    private Boolean updated;
    private List<LocalDate> updateDates = new ArrayList<>();
    private LocalDate publishDate;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "postId")
    private Post post;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private User user;

    public Comments(User userCommented, Post post, String content){
        this.content = content;
        this.post = post;
        this.user = userCommented;
        this.active = true;
        this.publishDate = LocalDate.now();
        this.updated = false;
    }

    public void updateComment(String content) {
        this.content = content;
        this.updateDates.add(LocalDate.now());
        if (!this.updated) {this.updated = true;}
    }
}
