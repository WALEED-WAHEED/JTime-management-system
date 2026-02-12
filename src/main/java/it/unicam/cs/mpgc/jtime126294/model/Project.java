package it.unicam.cs.mpgc.jtime126294.model;

import java.util.Collection;

/**
 * Represents a project containing a collection of tasks.
 */
public interface Project extends Taggable {
    /**
     * Gets the unique identifier of the project.
     * @return project ID
     */
    Long getId();

    /**
     * Gets the name of the project.
     * @return project name
     */
    String getName();

    /**
     * Gets the current state of the project.
     * @return project state
     */
    State getState();

    /**
     * Sets the state of the project.
     * @param state new state
     */
    void setState(State state);

    /**
     * Gets the tasks associated with this project.
     * @return collection of tasks
     */
    Collection<? extends Task> getTasks();

    /**
     * Adds a task to the project.
     * @param task task to add
     */
    void addTask(Task task);

    /**
     * Removes a task from the project.
     * @param task task to remove
     */
    void removeTask(Task task);
}
