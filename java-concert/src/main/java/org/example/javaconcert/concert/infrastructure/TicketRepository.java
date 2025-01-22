package org.example.javaconcert.concert.infrastructure;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.example.javaconcert.concert.infrastructure.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByTicketCode(String ticketCode);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Ticket t where t.concert.id = :concertId")
    List<Ticket> findAllByConcertIdForUpdate(@Param("concertId") Long concertId);

    List<Ticket> findAllByConcertId(Long concertId);

    int countByConcertId(Long concertId);
}
