package it.unicam.cs.mpgc.jtime126294.persistence;

import it.unicam.cs.mpgc.jtime126294.model.impl.ProjectImpl;
import it.unicam.cs.mpgc.jtime126294.persistence.entities.ProjectEntity;
import it.unicam.cs.mpgc.jtime126294.persistence.mapper.ProjectMapper;
import it.unicam.cs.mpgc.jtime126294.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class PersistenceService {
    
    public void saveAll(Collection<ProjectImpl> projects) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            // Clear existing data for simplicity in this prototype
            session.createMutationQuery("delete from TaskEntity").executeUpdate();
            session.createMutationQuery("delete from ProjectEntity").executeUpdate();
            session.createMutationQuery("delete from TagEntity").executeUpdate();
            
            for (ProjectImpl p : projects) {
                ProjectEntity entity = ProjectMapper.toEntity(p, session);
                // Nullify IDs to treat everything as new after the delete
                entity.setId(null);
                entity.getTasks().forEach(t -> t.setId(null));
                session.persist(entity);
            }
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Set<ProjectImpl> loadAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from ProjectEntity", ProjectEntity.class)
                    .list()
                    .stream()
                    .map(ProjectMapper::fromEntity)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
            return Set.of();
        }
    }
}
