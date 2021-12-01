package org.hexanome.model;

import javafx.scene.paint.Color;

public enum ColorEnum {
    BLUE(Color.BLUE),
    RED(Color.RED),
    GREEN(Color.GREEN),
    PURPLE(Color.PURPLE),
    AQUA(Color.AQUA),
    SILVER(Color.SILVER),
    DARKGOLDENROD(Color.DARKGOLDENROD),
    PINK(Color.PINK),
    YELLOW(Color.YELLOW),
    ANTIQUEWHITE(Color.ANTIQUEWHITE),
    SANDYBROWN(Color.SANDYBROWN),
    SALMON(Color.SALMON),
    DARKOLIVEGREEN(Color.DARKOLIVEGREEN),
    GREENYELLOW(Color.GREENYELLOW),
    STEELBLUE(Color.STEELBLUE);

    public final Color color;

    ColorEnum(javafx.scene.paint.Color color) {
        this.color = color;
    }
}
