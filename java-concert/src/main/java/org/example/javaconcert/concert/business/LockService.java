package org.example.javaconcert.concert.business;

import lombok.RequiredArgsConstructor;
import org.example.javaconcert.concert.infrastructure.LockRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockService {

    private final LockRepository lockRepository;

    public boolean getLock(String key) {
        return lockRepository.getLock(key);
    }

    public boolean releaseLock(String key) {
        return lockRepository.releaseLock(key);
    }

}
