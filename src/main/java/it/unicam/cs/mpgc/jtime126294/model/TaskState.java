package it.unicam.cs.mpgc.jtime126294.model;

/**
 * Enumeration representing the possible states of a task.
 */
public enum TaskState implements State {
    PENDING,
    IN_PROGRESS,
    COMPLETED {
        @Override
        public boolean isFinal() {
            return true;
        }
    },
    CANCELLED {
        @Override
        public boolean isFinal() {
            return true;
        }
    };

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public String getName() {
        return name();
    }
}
