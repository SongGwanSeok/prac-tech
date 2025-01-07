package org.example.javaconcert.concert.infrastructure;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.example.javaconcert.concert.infrastructure.entity.Concert;
import org.example.javaconcert.concert.infrastructure.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByTicketCode(String ticketCode);

    List<Ticket> findAllByConcert(Concert concert);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select count(t) from Ticket t where t.concert.id = :concertId")
    int countByConcertIdForShare(Long concertId);

    int countByConcertId(Long concertId);
}
