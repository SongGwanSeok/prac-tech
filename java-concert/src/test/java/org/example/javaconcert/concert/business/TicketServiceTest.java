package org.example.javaconcert.concert.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import org.example.javaconcert.concert.DbCleaner;
import org.example.javaconcert.concert.infrastructure.LockRepository;
import org.example.javaconcert.concert.infrastructure.entity.Concert;
import org.example.javaconcert.concert.infrastructure.entity.Genre;
import org.example.javaconcert.concert.infrastructure.entity.Region;
import org.example.javaconcert.concert.infrastructure.entity.Ticket;
import org.example.javaconcert.concert.presentation.dto.ConcertReserveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TicketServiceTest {

    @Autowired
    private DbCleaner dbCleaner;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ConcertService concertService;

    @Autowired
    private LockRepository lockRepository;

    private static final int TICKET_COUNT = 100;

    @BeforeEach
    void setUp() {
        dbCleaner.clean();

        Concert concert = Concert.builder()
            .title("다비치 콘서트")
            .genre(Genre.BALLADE)
            .region(Region.SEOUL)
            .place("올림픽 경기장")
            .startTime(LocalDateTime.of(2024, 12, 20, 0, 0, 0))
            .endTime(LocalDateTime.of(2024, 12, 23, 11, 59, 59))
            .totalTicketCount(TICKET_COUNT)
            .build();

        concertService.saveConcert(concert);
        System.out.println("================ setUp ================ ");
    }

    @Test
    @DisplayName("티켓 예매 테스트")
    void givenConcertWhenReserveTicketThenReturnTicket() {
        //given
        ConcertReserveRequest concertReserveRequest = new ConcertReserveRequest(1L, "송관석", "980902");

        //when
        //then
        assertDoesNotThrow(() -> ticketService.reserveTicket(concertReserveRequest));
    }

    @Test
    @DisplayName("티켓 예매 테스트 - 콘서트가 없는 경우")
    void givenConcertWhenNotFoundConcertThenThrowIllegalArgumentException() {
        //given
        ConcertReserveRequest concertReserveRequest = new ConcertReserveRequest(Long.MAX_VALUE, "송관석", "980902");

        //when
        //then
        assertThatThrownBy(() -> ticketService.reserveTicket(concertReserveRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("콘서트가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("티켓 예매 테스트 - 티켓 수량 초과")
    void givenEmptyTicketCountConcertWhenReserveMoreTicketThenThrowIllegalArgumentException() {
        //given
        ConcertReserveRequest concertReserveRequest = new ConcertReserveRequest(1L, "송관석", "980902");
        IntStream.range(0, TICKET_COUNT).forEach(i -> ticketService.reserveTicket(concertReserveRequest));

        //when
        Concert concert = getConcert();

        //then
        assertThatThrownBy(() -> ticketService.reserveTicket(concertReserveRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("콘서트 티켓 수량이 부족합니다");
    }

    @Test
    @DisplayName("10000명이 동시에 티켓 예매")
    void givenConcertWhenReserveTicketAtTheSameTimeThenThrowIllegalArgumentException() throws InterruptedException {
        //given
        ConcertReserveRequest concertReserveRequest = new ConcertReserveRequest(1L, "송관석", "980902");

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch countDownLatch = new CountDownLatch(10000);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        //when
        for (int i = 0; i < 10000; i++) {
            executorService.execute(() -> {
                try {
                    try {
                        lockRepository.getLock("reserveTicket");
                        Ticket ticket = ticketService.reserveTicket(concertReserveRequest);
                        System.out.println("ticket = " + ticket);
                    } catch (Exception e) {
                        throw new IllegalArgumentException(e.getMessage());
                    } finally {
                        lockRepository.releaseLock("reserveTicket");
                    }
                    successCount.getAndIncrement();
                } catch (IllegalArgumentException e) {
                    failCount.getAndIncrement();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        //then
        countDownLatch.await();
        executorService.shutdown();

        int ticketCount = ticketService.getTicketCount(1L);

        assertAll(
            () -> assertThat(ticketCount).as("티켓 수량 예상 실패").isEqualTo(100),
            () -> assertThat(successCount.get()).as("쓰레드 성공 개수 예상 실패").isEqualTo(100),
            () -> assertThat(failCount.get()).as("쓰레드 실패 개수 예상 실패").isEqualTo(9900)
        );
    }

    private Concert getConcert() {
        return concertService.getConcertById(1L);
    }
}
