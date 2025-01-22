package org.example.javaconcert.concert.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class LockRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean getLock(String lockName) {
        String query = "SELECT GET_LOCK(?, 3000)";
        Long result = (Long) entityManager.createNativeQuery(query)
            .setParameter(1, lockName)
            .getSingleResult();
        return result != null && result == 1;
    }

    public boolean releaseLock(String lockName) {
        String query = "SELECT RELEASE_LOCK(?)";
        Long result = (Long) entityManager.createNativeQuery(query)
            .setParameter(1, lockName)
            .getSingleResult();
        return result != null && result == 1;
    }
}
