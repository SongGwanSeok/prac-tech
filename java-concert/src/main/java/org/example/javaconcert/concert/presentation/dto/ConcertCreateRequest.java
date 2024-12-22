package org.example.javaconcert.concert.presentation.dto;

import java.time.LocalDateTime;
import org.example.javaconcert.concert.infrastructure.entity.Concert;
import org.example.javaconcert.concert.infrastructure.entity.Genre;
import org.example.javaconcert.concert.infrastructure.entity.Region;

public record ConcertCreateRequest(String title, Genre genre, Region region, String place, LocalDateTime startTime,
                                   LocalDateTime endTime) {

    public Concert toDomain() {
        return Concert.builder()
            .title(title)
            .genre(genre)
            .region(region)
            .place(place)
            .startTime(startTime)
            .endTime(endTime)
            .build();
    }
}
