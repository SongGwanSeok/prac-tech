package org.example.javaconcert.concert.infrastructure.entity;

import lombok.Getter;

@Getter
public enum Genre {
    BALLADE("발라드"),
    ROCK_METAL("락/메탈"),
    RAP_HIPHOP("랩/힙합"),
    JAZZ_SOUL("재즈/소울"),
    DINNER_SHOW("디너쇼"),
    FORK_TROT("포크/트로트"),
    PERFORMANCE_IN_KOREA("내한공연"),
    FESTIVAL("페스티벌"),
    FAN_MEETING("팬클럽/팬미팅"),
    INDIE("인디"),
    TALK_LECTURE("토크/강연");

    private final String name;

    Genre(String name) {
        this.name = name;
    }

    public static Genre of(String name) {
        return Genre.valueOf(name);
    }
}
