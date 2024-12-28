package org.example.javaconcert.concert.presentation.dto;

import java.time.LocalDateTime;
import org.example.javaconcert.concert.infrastructure.entity.Concert;
import org.example.javaconcert.concert.infrastructure.entity.Genre;
import org.example.javaconcert.concert.infrastructure.entity.Region;

public record ConcertResponse(
    Long id,
    String title,
    Genre genre,
    String place,
    Region region,
    LocalDateTime startTime,
    LocalDateTime endTime,
    int totalTicketCount
) {

    public static ConcertResponse ofConcert(Concert concert) {
        return new ConcertResponse(
            concert.getId(),
            concert.getTitle(),
            concert.getGenre(),
            concert.getPlace(),
            concert.getRegion(),
            concert.getStartTime(),
            concert.getEndTime(),
            concert.getTotalTicketCount()
        );
    }
}
