package org.example.javaconcert.concert.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
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

    @Version
    private Integer version;

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

    public Boolean canReserve(int reservedTicketCount) {
        return totalTicketCount > reservedTicketCount;
    }
}
