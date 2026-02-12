package it.unicam.cs.mpgc.jtime126294.model;

import java.time.Duration;

/**
 * Interface for entities that can be tracked in terms of time.
 */
public interface TimeTrackable {
    /**
     * Gets the estimated duration for the activity.
     * @return estimated duration
     */
    Duration getEstimatedTime();

    /**
     * Gets the actual duration recorded for the activity.
     * @return actual duration
     */
    Duration getActualTime();

    /**
     * Sets the actual duration for the activity.
     * @param duration actual duration
     */
    void setActualTime(Duration duration);
}
