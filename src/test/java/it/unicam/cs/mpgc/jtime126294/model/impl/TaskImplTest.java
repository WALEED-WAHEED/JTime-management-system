package it.unicam.cs.mpgc.jtime126294.model.impl;

import it.unicam.cs.mpgc.jtime126294.model.TaskState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class TaskImplTest {

    private TaskImpl task;

    @BeforeEach
    void setUp() {
        task = new TaskImpl(1L, "Task Name", "Sample Task", Duration.ofHours(2));
    }

    @Test
    void testInitialState() {
        assertEquals(TaskState.PENDING, task.getState());
        assertEquals("Task Name", task.getName());
        assertEquals("Sample Task", task.getDescription());
        assertEquals(Duration.ofHours(2), task.getEstimatedTime());
        assertEquals(Duration.ZERO, task.getActualTime());
    }

    @Test
    void testStateTransition() {
        task.setState(TaskState.IN_PROGRESS);
        assertEquals(TaskState.IN_PROGRESS, task.getState());

        task.setState(TaskState.COMPLETED);
        assertEquals(TaskState.COMPLETED, task.getState());

        // Test final state guard
        assertThrows(IllegalStateException.class, () -> task.setState(TaskState.PENDING));
    }

    @Test
    void testTagManagement() {
        task.addTag("Urgent");
        task.addTag("Work");
        
        assertTrue(task.getTags().contains("Urgent"));
        assertTrue(task.getTags().contains("Work"));
        
        task.removeTag("Urgent");
        assertFalse(task.getTags().contains("Urgent"));
        assertTrue(task.getTags().contains("Work"));
    }

    @Test
    void testActualTimeUpdate() {
        task.setActualTime(Duration.ofHours(3));
        assertEquals(Duration.ofHours(3), task.getActualTime());
    }
}
