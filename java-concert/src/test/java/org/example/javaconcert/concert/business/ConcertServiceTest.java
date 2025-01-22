package org.example.javaconcert.concert.business;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.example.javaconcert.concert.infrastructure.entity.Concert;
import org.example.javaconcert.concert.infrastructure.entity.Genre;
import org.example.javaconcert.concert.infrastructure.entity.Region;
import org.example.javaconcert.concert.presentation.dto.ConcertListResponse;
import org.example.javaconcert.concert.presentation.dto.ConcertResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ConcertServiceTest {

    @Autowired
    private ConcertService concertService;

    @Test
    void saveConcert() {
        //given
        concertService.saveConcert(Concert.builder()
            .title("다비치 콘서트")
            .genre(Genre.BALLADE)
            .region(Region.SEOUL)
            .place("올림픽 경기장")
            .startTime(LocalDateTime.of(2024, 12, 20, 0, 0, 0))
            .endTime(LocalDateTime.of(2024, 12, 23, 11, 59, 59))
            .totalTicketCount(10)
            .build());

        //when
        ConcertListResponse allConcerts = concertService.getAllConcerts();

        //then
        assertThat(allConcerts.getCount()).isEqualTo(1);
    }

    @Test
    void getConcert() {
        ConcertListResponse allConcerts = concertService.getAllConcerts();
        List<ConcertResponse> concerts = allConcerts.getConcerts();

        assertThat(concerts.size()).isEqualTo(0);
    }
}
