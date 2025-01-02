package org.example.javaconcert.concert.business;

import java.util.List;
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
        System.out.println("concert = " + concert);
        concert.decreaseTicketCount();
        return ticketRepository.save(concertReserveRequest.toTicket(concert));
    }

    @Transactional(readOnly = true)
    public Ticket getTicket(String ticketCode) {
        return ticketRepository.findByTicketCode(ticketCode)
            .orElseThrow(() -> new IllegalArgumentException("티켓 코드에 해당하는 티켓이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Integer getTicketCount(Long concertId) {
        Concert concert = concertService.getConcertById(concertId);
        List<Ticket> allByConcert = ticketRepository.findAllByConcert(concert);

        return allByConcert.size();
    }
}
