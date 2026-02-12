package it.unicam.cs.mpgc.jtime126294.model;

/**
 * Interface representing a generic state in a lifecycle.
 */
public interface State {
    /**
     * Returns true if the state is terminal.
     * @return true if final, false otherwise
     */
    boolean isFinal();

    /**
     * Returns the name of the state.
     * @return state name
     */
    String getName();
}
