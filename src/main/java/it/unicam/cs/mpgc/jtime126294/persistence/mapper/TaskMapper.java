package it.unicam.cs.mpgc.jtime126294.persistence.mapper;

import it.unicam.cs.mpgc.jtime126294.model.TaskState;
import it.unicam.cs.mpgc.jtime126294.model.impl.TaskImpl;
import it.unicam.cs.mpgc.jtime126294.persistence.entities.TagEntity;
import it.unicam.cs.mpgc.jtime126294.persistence.entities.TaskEntity;
import org.hibernate.Session;

import java.util.Set;
import java.util.stream.Collectors;

public class TaskMapper {
    public static TaskEntity toEntity(TaskImpl domain, Session session) {
        if (domain == null) return null;
        
        TaskEntity entity = new TaskEntity(
                domain.getName(),
                domain.getDescription(),
                (TaskState) domain.getState(),
                domain.getEstimatedTime()
        );
        entity.setId(domain.getId());
        entity.setActualTime(domain.getActualTime());
        entity.setPlannedDate(domain.getPlannedDate());
        
        Set<TagEntity> tagEntities = domain.getTags().stream()
                .map(tagName -> {
                    // Try to find existing tag or create new
                    TagEntity tag = session.createQuery("from TagEntity where name = :name", TagEntity.class)
                            .setParameter("name", tagName)
                            .uniqueResult();
                    if (tag == null) {
                        tag = new TagEntity(tagName);
                        session.persist(tag);
                    }
                    return tag;
                })
                .collect(Collectors.toSet());
        entity.setTags(tagEntities);
        
        return entity;
    }

    public static TaskImpl fromEntity(TaskEntity entity) {
        if (entity == null) return null;
        
        TaskImpl domain = new TaskImpl(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getEstimatedTime()
        );
        domain.setState(entity.getState());
        domain.setActualTime(entity.getActualTime());
        domain.setPlannedDate(entity.getPlannedDate());
        entity.getTags().forEach(tag -> domain.addTag(tag.getName()));
        
        return domain;
    }
}
