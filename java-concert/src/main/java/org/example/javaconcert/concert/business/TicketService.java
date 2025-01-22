package org.example.javaconcert.concert.business;

import org.example.javaconcert.concert.infrastructure.TicketRepository;
import org.example.javaconcert.concert.infrastructure.entity.Concert;
import org.example.javaconcert.concert.infrastructure.entity.Ticket;
import org.example.javaconcert.concert.presentation.dto.ConcertReserveRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ConcertService concertService;

    public TicketService(TicketRepository ticketRepository, ConcertService concertService) {
        this.ticketRepository = ticketRepository;
        this.concertService = concertService;
    }

    @Transactional
    public synchronized Ticket reserveTicket(ConcertReserveRequest concertReserveRequest) {
        Concert concert = concertService.getConcertById(concertReserveRequest.concertId());
        int reservedTicketCount = getTicketCount(concert.getId());
        if (!concert.canReserve(reservedTicketCount)) {
            throw new IllegalArgumentException("콘서트 티켓 수량이 부족합니다");
        }
        return ticketRepository.save(concertReserveRequest.toTicket(concert));
    }

    @Transactional(readOnly = true)
    public Ticket getTicket(String ticketCode) {
        return ticketRepository.findByTicketCode(ticketCode)
            .orElseThrow(() -> new IllegalArgumentException("티켓 코드에 해당하는 티켓이 존재하지 않습니다."));
    }

    @Transactional
    public Integer getTicketCount(Long concertId) {
        return ticketRepository.findAllByConcertId(concertId).size();
    }
}
