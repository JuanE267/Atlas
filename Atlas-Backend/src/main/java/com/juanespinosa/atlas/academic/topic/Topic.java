package com.juanespinosa.atlas.academic.topic;

import com.juanespinosa.atlas.academic.subject.Subject;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "topic")
@Getter
@Setter
@NoArgsConstructor
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
}