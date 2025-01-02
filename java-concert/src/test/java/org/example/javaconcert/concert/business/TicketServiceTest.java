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
import org.example.javaconcert.concert.infrastructure.ConcertRepository;
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

@SpringBootTest
class TicketServiceTest {

    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private TicketService ticketService;

    private static final int TICKET_COUNT = 10;
    private Long savedConcertId;

    @BeforeEach
    void setUp() {
        Concert concert = Concert.builder()
            .title("다비치 콘서트")
            .genre(Genre.BALLADE)
            .region(Region.SEOUL)
            .place("올림픽 경기장")
            .startTime(LocalDateTime.of(2024, 12, 20, 0, 0, 0))
            .endTime(LocalDateTime.of(2024, 12, 23, 11, 59, 59))
            .totalTicketCount(TICKET_COUNT)
            .build();

        Concert savedConcert = concertRepository.save(concert);
        savedConcertId = savedConcert.getId();
    }

    @Test
    @DisplayName("티켓 예매 테스트")
    void givenConcertWhenReserveTicketThenReturnTicket() {
        //given
        ConcertReserveRequest concertReserveRequest = new ConcertReserveRequest(savedConcertId, "송관석", "980902");

        //when
        //then
        Ticket reservedTicket = assertDoesNotThrow(() -> ticketService.reserveTicket(concertReserveRequest));
        Concert concert = reservedTicket.getConcert();

        assertThat(concert.getTotalTicketCount()).isEqualTo(9);
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
        ConcertReserveRequest concertReserveRequest = new ConcertReserveRequest(savedConcertId, "송관석", "980902");
        IntStream.range(0, TICKET_COUNT).forEach(i -> ticketService.reserveTicket(concertReserveRequest));

        //when
        Concert concert = getConcert();

        //then
        assertThat(concert.getTotalTicketCount()).isEqualTo(0);
        assertThatThrownBy(() -> ticketService.reserveTicket(concertReserveRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("콘서트 티켓이 부족합니다.");
    }

    @Test
    @DisplayName("15명이 동시에 티켓 예매")
    void givenConcertWhenReserveTicketAtTheSameTimeThenThrowIllegalArgumentException() throws InterruptedException {
        //given
        ConcertReserveRequest concertReserveRequest = new ConcertReserveRequest(savedConcertId, "송관석", "980902");
        int numThreads = 15;

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        CountDownLatch countDownLatch = new CountDownLatch(numThreads);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        //when
        for (int i = 0; i < numThreads; i++) {
            executorService.execute(() -> {
                try {
                    Ticket ticket = ticketService.reserveTicket(concertReserveRequest);
                    System.out.println("ticket = " + ticket);
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

//        Concert concert = getConcert();
//        int ticketCount = ticketService.getTicketCount(savedConcertId);

        assertAll(
//            // concert의 티켓 수량은 0개가 되어야 한다.
//            () -> assertThat(concert.getTotalTicketCount()).isEqualTo(0),
//            // 발행된 티켓은 10개가 되어야 한다.
//            () -> assertThat(ticketCount).isEqualTo(10),

            // 성공한 쓰레드는 10개여야 한다.
            () -> assertThat(successCount.get()).isEqualTo(10),
            // 실패한 쓰레드는 5개여야 한다.
            () -> assertThat(failCount.get()).isEqualTo(5)
        );
    }

    private Concert getConcert() {
        return concertRepository.findById(savedConcertId)
            .orElseThrow(() -> new IllegalArgumentException("[테스트] 콘서트를 가져올 수 없습니다."));
    }
}
