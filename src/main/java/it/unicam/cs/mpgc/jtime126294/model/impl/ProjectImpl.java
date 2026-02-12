package it.unicam.cs.mpgc.jtime126294.model.impl;

import it.unicam.cs.mpgc.jtime126294.model.Project;
import it.unicam.cs.mpgc.jtime126294.model.ProjectState;
import it.unicam.cs.mpgc.jtime126294.model.State;
import it.unicam.cs.mpgc.jtime126294.model.Task;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Concrete implementation of a Project.
 */
@Getter
@Setter
public class ProjectImpl implements Project {
    private Long id;
    private String name;
    private State state;
    private Set<Task> tasks;
    private Set<String> tags;

    public ProjectImpl(Long id, String name) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.state = ProjectState.ACTIVE;
        this.tasks = new HashSet<>();
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
    public Collection<? extends Task> getTasks() {
        return new HashSet<>(tasks);
    }

    @Override
    public void addTask(Task task) {
        tasks.add(Objects.requireNonNull(task));
    }

    @Override
    public void removeTask(Task task) {
        tasks.remove(task);
    }

    @Override
    public void setState(State state) {
        this.state = Objects.requireNonNull(state);
    }
}
