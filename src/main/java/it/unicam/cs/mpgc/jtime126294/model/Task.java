package it.unicam.cs.mpgc.jtime126294.model;

import java.time.LocalDate;

/**
 * Represents a task within a project.
 */
public interface Task extends Taggable, TimeTrackable {
    /**
     * Gets the unique identifier of the task.
     * @return task ID
     */
    Long getId();

    /**
     * Gets the description of the task.
     * @return task description
     */
    String getDescription();

    /**
     * Gets the current state of the task.
     * @return task state
     */
    State getState();

    /**
     * Sets the state of the task.
     * @param state new state
     */
    void setState(State state);

    /**
     * Gets the date the task is planned for, if any.
     * @return planned date
     */
    LocalDate getPlannedDate();

    /**
     * Sets the planned date for the task.
     * @param date planned date
     */
    void setPlannedDate(LocalDate date);
}
