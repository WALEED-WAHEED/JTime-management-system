package it.unicam.cs.mpgc.jtime126294.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

/**
 * Interface for generating summary reports.
 */
public interface ReportManager {
    /**
     * Generates a report organized by project.
     * @return a map where keys are project names and values are collections of tasks
     */
    Map<String, Collection<? extends Task>> getReportByProject();

    /**
     * Generates a report for a specific time interval.
     * @param start start date
     * @param end end date
     * @return collection of tasks in the interval
     */
    Collection<? extends Task> getReportByInterval(LocalDate start, LocalDate end);

    /**
     * Gets tasks completed within a specific time interval.
     * @param start start date
     * @param end end date
     * @return collection of completed tasks in the interval
     */
    Collection<? extends Task> getCompletedTasks(LocalDate start, LocalDate end);
}
