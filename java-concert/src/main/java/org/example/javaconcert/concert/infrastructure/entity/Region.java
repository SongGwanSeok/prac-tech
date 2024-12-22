package org.example.javaconcert.concert.infrastructure.entity;

import lombok.Getter;

@Getter
public enum Region {
    SEOUL("서울"),
    GYEONGGI_GANGWON("경기/강원"),
    GYEONGSANG("경상"),
    JEOLLA("전라"),
    JEJU("제주"),
    CHUNGCHEONG("충청");

    private final String name;

    Region(String name) {
        this.name = name;
    }

    public static Region of(String name) {
        return Region.valueOf(name);
    }
}
