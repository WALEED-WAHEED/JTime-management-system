package it.unicam.cs.mpgc.jtime126294.model.impl;

import it.unicam.cs.mpgc.jtime126294.model.State;
import it.unicam.cs.mpgc.jtime126294.model.Task;
import it.unicam.cs.mpgc.jtime126294.model.TaskState;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Concrete implementation of a Task.
 */
@Getter
@Setter
public class TaskImpl implements Task {
    private Long id;
    private String description;
    private State state;
    private Duration estimatedTime;
    private Duration actualTime;
    private LocalDate plannedDate;
    private Set<String> tags;

    public TaskImpl(Long id, String description, Duration estimatedTime) {
        this.id = id;
        this.description = Objects.requireNonNull(description);
        this.estimatedTime = Objects.requireNonNull(estimatedTime);
        this.state = TaskState.PENDING;
        this.actualTime = Duration.ZERO;
        this.tags = new HashSet<>();
    }

    @Override
    public Set<String> getTags() {
        return new HashSet<>(tags);
    }

    @Override
    public void addTag(String tag) {
        tags.add(tag);
    }

    @Override
    public void removeTag(String tag) {
        tags.remove(tag);
    }

    @Override
    public void setActualTime(Duration duration) {
        this.actualTime = Objects.requireNonNull(duration);
    }

    @Override
    public void setState(State state) {
        if (this.state.isFinal()) {
            throw new IllegalStateException("Cannot change state of a finished task");
        }
        this.state = Objects.requireNonNull(state);
    }
}
