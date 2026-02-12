package it.unicam.cs.mpgc.jtime126294.model;

/**
 * Enumeration representing the possible states of a project.
 */
public enum ProjectState implements State {
    ACTIVE,
    COMPLETED {
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
