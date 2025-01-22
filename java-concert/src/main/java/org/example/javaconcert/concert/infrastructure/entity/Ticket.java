package org.example.javaconcert.concert.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    private String ticketCode;
    private String reservationName;
    private String reservationBirth;

    @CreatedDate
    private LocalDateTime reservationTime;
    @ColumnDefault(value = "FALSE")
    private Boolean isIssued;

    @Version
    private long version;

    public Ticket() {
    }

    public Ticket(Concert concert, String reservationName, String reservationBirth) {
        this.ticketCode = makeTicketCode();
        this.concert = concert;
        this.reservationName = reservationName;
        this.reservationBirth = reservationBirth;
    }

    private String makeTicketCode() {
        return UUID.randomUUID().toString();
    }
}
