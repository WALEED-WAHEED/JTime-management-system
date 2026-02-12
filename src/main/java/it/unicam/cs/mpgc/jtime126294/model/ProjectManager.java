package it.unicam.cs.mpgc.jtime126294.model;

import java.util.Collection;

/**
 * Manager interface for project and task operations.
 *
 * @param <P> the type of projects managed
 * @param <T> the type of tasks managed
 */
public interface ProjectManager<P extends Project, T extends Task> {
    /**
     * Creates and adds a new project.
     * @param name project name
     * @return the created project
     */
    P createProject(String name);

    /**
     * Gets all projects.
     * @return collection of projects
     */
    Collection<P> getProjects();

    /**
     * Closes a project if there are no pending tasks.
     * @param project the project to close
     * @return true if closed successfully, false otherwise
     */
    boolean closeProject(P project);

    /**
     * Deletes a project.
     * @param project the project to delete
     */
    void deleteProject(P project);

    /**
     * Adds a task to a project.
     * @param project project to add to
     * @param description task description
     * @param estimate estimated duration
     * @return the created task
     */
    T addTaskToProject(P project, String description, java.time.Duration estimate);

    /**
     * Completes a task.
     * @param task the task to complete
     * @param actualTime actual duration
     */
    void completeTask(T task, java.time.Duration actualTime);

    /**
     * Deletes a task from its project.
     * @param task the task to delete
     */
    void deleteTask(T task);
}
