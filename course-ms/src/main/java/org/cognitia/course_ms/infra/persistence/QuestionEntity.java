package org.cognitia.course_ms.infra.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDateTime questionedAt;

    private String authorId;

    private String authorProfileUrl;

    private Long videoId;

    private Long path;

    private Long courseId;

    private String authorName;

    private List<Long> upvotes;


    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionEntity> answers; // sub-perguntas

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private QuestionEntity parent; // pergunta que originou essa

    public void addUpVote(Long upVoteId){
        this.upvotes.add(upVoteId);
    }

    public void removeUpVote(Long upVoteId){
        this.upvotes.remove(upVoteId);
    }

}
