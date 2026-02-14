package it.unicam.cs.mpgc.jtime126294.persistence.entities;

import it.unicam.cs.mpgc.jtime126294.model.TaskState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskState state;

    private Duration estimatedTime;
    private Duration actualTime;
    private LocalDate plannedDate;
    private LocalDate completionDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "task_tags",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagEntity> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    public TaskEntity(String name, String description, TaskState state, Duration estimatedTime) {
        this.name = name;
        this.description = description;
        this.state = state;
        this.estimatedTime = estimatedTime;
        this.actualTime = Duration.ZERO;
    }
}
