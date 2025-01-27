package org.example.javaconcert.concert.business;

import lombok.RequiredArgsConstructor;
import org.example.javaconcert.concert.infrastructure.LockRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockService {

    private final LockRepository lockRepository;
    private final RedissonClient redissonClient;

    public boolean getLock(String key) {
        return lockRepository.getLock(key);
    }

    public void releaseLock(String key) {
        lockRepository.releaseLock(key);
    }

    public RLock getLockByRedis(String key) {
        return redissonClient.getLock(key);
    }

}
