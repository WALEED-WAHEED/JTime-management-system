package it.unicam.cs.mpgc.jtime126294.model;

import java.time.LocalDate;
import java.util.Collection;

/**
 * Manager interface for daily planning operations.
 *
 * @param <T> the type of tasks managed
 */
public interface PlanningManager<T extends Task> {
    /**
     * Plans a task for a specific day.
     * @param task the task to plan
     * @param date the date for the task
     */
    void planTask(T task, LocalDate date);

    /**
     * Gets tasks planned for a specific day.
     * @param date the date to check
     * @return collection of planned tasks
     */
    Collection<T> getTasksForDay(LocalDate date);

    /**
     * Gets the total effort (estimated duration) for a specific day.
     * @param date the date to check
     * @return total duration
     */
    java.time.Duration getTotalEffortForDay(LocalDate date);
}
