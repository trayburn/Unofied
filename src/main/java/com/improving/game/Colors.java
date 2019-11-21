package com.improving.game;

public enum Colors {
    Red ("red",1),
    Green ("green",2),
    Blue ("blue",3),
    Yellow ("yellow",4),
    Wild ("wild",5);

    private final String colorName;
    public final int pointValue;
    Colors(String colorName, int pointValue) {

        this.colorName = colorName;
        this.pointValue = pointValue;
    }

    public String getColorName() {
        return colorName;
    }

    public Object getPointValue() {
        return pointValue;
    }


}
