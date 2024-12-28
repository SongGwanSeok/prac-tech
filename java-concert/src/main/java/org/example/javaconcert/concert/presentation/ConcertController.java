package org.example.javaconcert.concert.presentation;

import org.example.javaconcert.concert.business.ConcertService;
import org.example.javaconcert.concert.business.TicketService;
import org.example.javaconcert.concert.infrastructure.entity.Ticket;
import org.example.javaconcert.concert.presentation.dto.ConcertCreateRequest;
import org.example.javaconcert.concert.presentation.dto.ConcertListResponse;
import org.example.javaconcert.concert.presentation.dto.ConcertReserveRequest;
import org.example.javaconcert.concert.presentation.dto.SuccessResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConcertController {

    private final ConcertService concertService;
    private final TicketService ticketService;

    public ConcertController(ConcertService concertService, TicketService ticketService) {
        this.concertService = concertService;
        this.ticketService = ticketService;
    }

    @PostMapping("/concert")
    public SuccessResponse<Long> saveConcert(@RequestBody ConcertCreateRequest concertCreateRequest) {
        Long concertId = concertService.saveConcert(concertCreateRequest.toConcert());

        return SuccessResponse.withData("성공적으로 콘서트를 생성했습니다.", concertId);
    }

    @GetMapping("/concert")
    public SuccessResponse<ConcertListResponse> getConcerts() {
        ConcertListResponse allConcerts = concertService.getAllConcerts();

        return SuccessResponse.withData("성공적으로 콘서트 리스트를 조회헀습니다.", allConcerts);
    }

    @PostMapping("/concert/reservation")
    public SuccessResponse<Ticket> reserveConcert(@RequestBody ConcertReserveRequest concertReserveRequest) {
        Ticket ticket = ticketService.reserveTicket(concertReserveRequest);

        return SuccessResponse.withData("성공적으로 티켓 예약에 성공했습니다.", ticket);
    }
}
