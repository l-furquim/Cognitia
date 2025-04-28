package org.cognitia.course_ms.infra.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@ToString
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

    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionEntity> answers; // sub-perguntas


    @JsonBackReference
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private QuestionEntity parent; // pergunta que originou essa

    public void addNewQuestion(QuestionEntity questionEntity){
        this.answers.add(questionEntity);
    }

    public void removeQuestion(QuestionEntity question){
        this.answers.remove(question);
    }

}
