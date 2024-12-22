package org.example.javaconcert.concert.presentation.dto;

import java.util.List;
import lombok.Getter;
import org.example.javaconcert.concert.infrastructure.entity.Concert;

@Getter
public class ConcertListResponse {

    private final int count;
    private final List<ConcertResponse> concerts;

    public ConcertListResponse(List<Concert> concerts) {
        this.concerts = concerts.stream()
            .map(ConcertResponse::ofConcert)
            .toList();
        this.count = concerts.size();
    }
}
