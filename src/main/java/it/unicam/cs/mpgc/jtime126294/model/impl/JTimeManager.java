package it.unicam.cs.mpgc.jtime126294.model.impl;

import it.unicam.cs.mpgc.jtime126294.model.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Central manager for JTime application.
 * Uses composition to implement project, planning, and reporting logic.
 */
public class JTimeManager implements 
        ProjectManager<ProjectImpl, TaskImpl>, 
        PlanningManager<TaskImpl>, 
        ReportManager {

    private final Set<ProjectImpl> projects;
    private Long nextProjectId = 1L;
    private Long nextTaskId = 1L;

    public JTimeManager() {
        this.projects = new HashSet<>();
    }

    public JTimeManager(Set<ProjectImpl> projects) {
        this.projects = Objects.requireNonNull(projects);
        // Initialize IDs based on existing data if needed
    }

    @Override
    public ProjectImpl createProject(String name) {
        ProjectImpl project = new ProjectImpl(nextProjectId++, name);
        projects.add(project);
        return project;
    }

    @Override
    public Collection<ProjectImpl> getProjects() {
        return new HashSet<>(projects);
    }

    @Override
    public boolean closeProject(ProjectImpl project) {
        if (!projects.contains(project)) return false;
        
        boolean hasPending = project.getTasks().stream()
                .anyMatch(t -> !t.getState().isFinal());
        
        if (!hasPending) {
            project.setState(ProjectState.COMPLETED);
            return true;
        }
        return false;
    }

    @Override
    public void deleteProject(ProjectImpl project) {
        projects.remove(project);
    }

    @Override
    public TaskImpl addTaskToProject(ProjectImpl project, String description, Duration estimate) {
        if (!projects.contains(project)) throw new IllegalArgumentException("Project not managed");
        
        TaskImpl task = new TaskImpl(nextTaskId++, description, estimate);
        project.addTask(task);
        return task;
    }

    @Override
    public void completeTask(TaskImpl task, Duration actualTime) {
        task.setActualTime(actualTime);
        task.setState(TaskState.COMPLETED);
    }

    @Override
    public void deleteTask(TaskImpl task) {
        for (ProjectImpl p : projects) {
            if (p.getTasks().contains(task)) {
                p.removeTask(task);
                return;
            }
        }
    }

    @Override
    public void planTask(TaskImpl task, LocalDate date) {
        task.setPlannedDate(date);
    }

    @Override
    public Collection<TaskImpl> getTasksForDay(LocalDate date) {
        return projects.stream()
                .flatMap(p -> p.getTasks().stream())
                .filter(t -> Objects.equals(t.getPlannedDate(), date))
                .filter(t -> t instanceof TaskImpl)
                .map(t -> (TaskImpl) t)
                .collect(Collectors.toList());
    }

    @Override
    public Duration getTotalEffortForDay(LocalDate date) {
        return getTasksForDay(date).stream()
                .map(Task::getEstimatedTime)
                .reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    public Map<String, Collection<? extends Task>> getReportByProject() {
        return projects.stream()
                .collect(Collectors.toMap(
                        Project::getName,
                        Project::getTasks,
                        (a, b) -> a));
    }

    @Override
    public Collection<? extends Task> getReportByInterval(LocalDate start, LocalDate end) {
        return projects.stream()
                .flatMap(p -> p.getTasks().stream())
                .filter(t -> t.getPlannedDate() != null && 
                            !t.getPlannedDate().isBefore(start) && 
                            !t.getPlannedDate().isAfter(end))
                .collect(Collectors.toList());
    }
}
