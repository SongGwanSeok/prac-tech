package org.example.javaconcert.concert.business;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.example.javaconcert.concert.infrastructure.ConcertRepository;
import org.example.javaconcert.concert.infrastructure.entity.Concert;
import org.example.javaconcert.concert.infrastructure.entity.Genre;
import org.example.javaconcert.concert.infrastructure.entity.Region;
import org.example.javaconcert.concert.presentation.dto.ConcertListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ConcertServiceTest {

    @Autowired
    private ConcertService concertService;
    @Autowired
    private ConcertRepository concertRepository;

    @Test
    @DisplayName("Concert 생성 테스트")
    void givenConcertWhenSaveConcertThenReturnConcertId() {
        //given
        Concert concert = createConcert();

        //when
        Long actual = concertService.saveConcert(concert);

        //then
        Concert findConcert = concertRepository.findById(actual)
            .orElseThrow(() -> new IllegalArgumentException("데이터가 저장되지 않았습니다."));
        Long expected = findConcert.getId();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Concert list 조회 테스트")
    void whenGetConcertsThenReturnConcertList() {
        //given
        concertService.saveConcert(createConcert());
        concertService.saveConcert(createConcert());

        //when
        ConcertListResponse allConcerts = concertService.getAllConcerts();

        //then
        assertThat(allConcerts.getCount()).isEqualTo(2);
    }

    private Concert createConcert() {
        return new Concert(
            "2025 DAVICHI CONCERT 〈A Stitch in Time〉",
            Genre.BALLADE,
            Region.SEOUL,
            "KSPO DOME(올림픽공원)",
            LocalDateTime.of(2025, 1, 18, 0, 0),
            LocalDateTime.of(2025, 1, 19, 23, 59)
        );
    }
}
