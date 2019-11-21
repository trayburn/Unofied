package com.improving.game;

public enum Faces {
    Zero (0,0),
    One (1,1),
    Two (2,2),
    Three (3,3),
    Four (4,4),
    Five (5,5),
    Six (6,6),
    Seven (7,7),
    Eight (8,8),
    Nine (9,9),
    Reverse (20,10),
    Skip (20,11),
    Draw_2 (20,12),
    Draw_4 (50,13),
    Wild (50,14);

    private final int value;
    private final int pointValue;

    Faces(int value, int pointValue) {

        this.value = value;
        this.pointValue =pointValue;
    }

    public int getValue() {
        return value;
    }


    public int getPointValue() {
        return pointValue;
    }
}
