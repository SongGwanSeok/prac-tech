package org.example.javaconcert.concert.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(value = EnumType.STRING)
    private Genre genre;
    @Enumerated(value = EnumType.STRING)
    private Region region;

    private String place;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int totalTicketCount;

    public Concert() {
    }

    @Builder
    public Concert(String title, Genre genre, Region region, String place, LocalDateTime startTime,
        LocalDateTime endTime, int totalTicketCount) {
        this.title = title;
        this.genre = genre;
        this.region = region;
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalTicketCount = totalTicketCount;
    }

    public void decreaseTicketCount() {
        if (totalTicketCount <= 0) {
            throw new IllegalArgumentException("콘서트 티켓이 부족합니다.");
        }
        this.totalTicketCount--;
    }
}
