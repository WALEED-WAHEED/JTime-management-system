package it.unicam.cs.mpgc.jtime126294.persistence.mapper;

import it.unicam.cs.mpgc.jtime126294.model.ProjectState;
import it.unicam.cs.mpgc.jtime126294.model.impl.ProjectImpl;
import it.unicam.cs.mpgc.jtime126294.persistence.entities.ProjectEntity;
import it.unicam.cs.mpgc.jtime126294.persistence.entities.TagEntity;
import it.unicam.cs.mpgc.jtime126294.persistence.entities.TaskEntity;
import it.unicam.cs.mpgc.jtime126294.model.impl.TaskImpl;
import org.hibernate.Session;

import java.util.Set;
import java.util.stream.Collectors;

public class ProjectMapper {
    public static ProjectEntity toEntity(ProjectImpl domain, Session session) {
        if (domain == null) return null;
        
        ProjectEntity entity = new ProjectEntity(domain.getName(), (ProjectState) domain.getState());
        entity.setId(domain.getId());
        
        Set<TaskEntity> taskEntities = domain.getTasks().stream()
                .filter(t -> t instanceof TaskImpl)
                .map(t -> {
                    TaskEntity te = TaskMapper.toEntity((TaskImpl) t, session);
                    te.setProject(entity);
                    return te;
                })
                .collect(Collectors.toSet());
        entity.setTasks(taskEntities);
        
        Set<TagEntity> tagEntities = domain.getTags().stream()
                .map(tagName -> {
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

    public static ProjectImpl fromEntity(ProjectEntity entity) {
        if (entity == null) return null;
        
        ProjectImpl domain = new ProjectImpl(entity.getId(), entity.getName());
        domain.setState(entity.getState());
        entity.getTasks().forEach(te -> domain.addTask(TaskMapper.fromEntity(te)));
        entity.getTags().forEach(tag -> domain.addTag(tag.getName()));
        
        return domain;
    }
}
