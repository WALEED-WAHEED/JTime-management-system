package it.unicam.cs.mpgc.jtime126294;

import it.unicam.cs.mpgc.jtime126294.model.Task;
import it.unicam.cs.mpgc.jtime126294.model.TaskState;
import it.unicam.cs.mpgc.jtime126294.model.impl.JTimeManager;
import it.unicam.cs.mpgc.jtime126294.model.impl.ProjectImpl;
import it.unicam.cs.mpgc.jtime126294.model.impl.TaskImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class JTimeManagerTest {
    
    private JTimeManager manager;
    private ProjectImpl project;

    @BeforeEach
    void setUp() {
        manager = new JTimeManager();
        project = manager.createProject("Test Project");
    }

    @Test
    void testTaskCompletionDate() {
        TaskImpl task = manager.addTaskToProject(project, "Task 1", "Desc", Duration.ofHours(1));
        assertNull(task.getCompletionDate());
        
        manager.completeTask(task, Duration.ofHours(2));
        
        assertEquals(TaskState.COMPLETED, task.getState());
        assertEquals(LocalDate.now(), task.getCompletionDate());
    }

    @Test
    void testGetCompletedTasksInInterval() {
        TaskImpl task1 = manager.addTaskToProject(project, "Task 1", "Desc", Duration.ofHours(1));
        TaskImpl task2 = manager.addTaskToProject(project, "Task 2", "Desc", Duration.ofHours(1));
        
        manager.completeTask(task1, Duration.ofHours(1));
        // Task 2 is not completed
        
        LocalDate today = LocalDate.now();
        Collection<? extends Task> completedToday = manager.getCompletedTasks(today, today);
        
        assertEquals(1, completedToday.size());
        assertTrue(completedToday.contains(task1));
        
        // Test interval boundaries
        assertTrue(manager.getCompletedTasks(today.minusDays(1), today).contains(task1));
        assertTrue(manager.getCompletedTasks(today, today.plusDays(1)).contains(task1));
        assertFalse(manager.getCompletedTasks(today.minusDays(2), today.minusDays(1)).contains(task1));
    }

    @Test
    void testDeleteProject() {
        assertFalse(manager.getProjects().isEmpty());
        manager.deleteProject(project);
        assertTrue(manager.getProjects().isEmpty());
    }
}
