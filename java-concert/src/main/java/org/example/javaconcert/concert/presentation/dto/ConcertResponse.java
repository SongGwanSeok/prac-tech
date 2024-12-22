package org.example.javaconcert.concert.presentation.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import org.example.javaconcert.concert.infrastructure.entity.Concert;
import org.example.javaconcert.concert.infrastructure.entity.Genre;
import org.example.javaconcert.concert.infrastructure.entity.Region;

@Getter
public class ConcertResponse {

    private final String title;
    private final Genre genre;
    private final Region region;
    private final String place;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public ConcertResponse(String title, Genre genre, String place, Region region,
        LocalDateTime startTime, LocalDateTime endTime) {
        this.title = title;
        this.genre = genre;
        this.region = region;
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static ConcertResponse ofConcert(Concert concert) {
        return new ConcertResponse(
            concert.getTitle(),
            concert.getGenre(),
            concert.getPlace(),
            concert.getRegion(),
            concert.getStartTime(),
            concert.getEndTime()
        );
    }
}
