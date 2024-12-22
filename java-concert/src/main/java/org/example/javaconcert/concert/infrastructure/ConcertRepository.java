package org.example.javaconcert.concert.infrastructure;

import org.example.javaconcert.concert.infrastructure.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {

}
