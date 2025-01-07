package org.example.javaconcert.concert.infrastructure;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.example.javaconcert.concert.infrastructure.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select c from Concert c where c.id = :id")
    Optional<Concert> findByIdForShare(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Concert c where c.id = :id")
    Optional<Concert> findByIdForUpdate(@Param("id") Long id);
}
