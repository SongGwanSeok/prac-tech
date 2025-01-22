package org.example.javaconcert.concert.business;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.javaconcert.concert.infrastructure.ConcertRepository;
import org.example.javaconcert.concert.infrastructure.entity.Concert;
import org.example.javaconcert.concert.presentation.dto.ConcertListResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    @Transactional
    public Long saveConcert(Concert concert) {
        Concert savedConcert = concertRepository.save(concert);

        return savedConcert.getId();
    }

    @Transactional(readOnly = true)
    public ConcertListResponse getAllConcerts() {
        List<Concert> concerts = concertRepository.findAll();

        return new ConcertListResponse(concerts);
    }

    @Transactional(readOnly = true)
    public Concert getConcertById(Long concertId) {
        return concertRepository.findById(concertId)
            .orElseThrow(() -> new IllegalArgumentException("콘서트가 존재하지 않습니다."));
    }
}
