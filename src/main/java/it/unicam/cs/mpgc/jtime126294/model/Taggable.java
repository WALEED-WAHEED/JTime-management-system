package it.unicam.cs.mpgc.jtime126294.model;

import java.util.Set;

/**
 * Interface representing an entity that can be associated with tags.
 */
public interface Taggable {
    /**
     * Returns the set of tags associated with this entity.
     * @return a set of tags
     */
    Set<String> getTags();

    /**
     * Adds a tag to this entity.
     * @param tag the tag to add
     */
    void addTag(String tag);

    /**
     * Removes a tag from this entity.
     * @param tag the tag to remove
     */
    void removeTag(String tag);
}
