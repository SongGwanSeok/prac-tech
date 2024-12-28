package org.example.javaconcert.concert.presentation.dto;

import org.example.javaconcert.concert.infrastructure.entity.Concert;
import org.example.javaconcert.concert.infrastructure.entity.Ticket;

public record ConcertReserveRequest(
    Long concertId,
    String reservationName,
    String reservationBirth
) {

    public Ticket toTicket(Concert concert) {
        return new Ticket(concert, this.reservationName, this.reservationBirth);
    }
}
