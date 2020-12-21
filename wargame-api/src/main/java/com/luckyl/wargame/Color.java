package com.luckyl.wargame;

import org.apache.commons.lang3.StringUtils;

public enum Color {
    BLUE("#3366CC"),
    BLUE_DOWN("#000099"),
    RED("#FF0000"),
    RED_DOWN("#800000"),
    WHITE("#FFFFFF"),
    GRAY("#808080"),
    INVALID("");

    private final String rgbColor;

    Color(String rgbColor) {
        this.rgbColor = StringUtils.isNotBlank(rgbColor) ? rgbColor : "";
    }

    public String getRgb() {
        return rgbColor;
    }

    @Override
    public String toString() {
        return rgbColor;
    }

    public static Color fromRgb(String rgbColor) {
        if (rgbColor == null)
            return Color.INVALID;
        else
            return switch(rgbColor) {
                case "#3366CC" -> BLUE;
                case "#000099" -> BLUE_DOWN;
                case "#FF0000" -> RED;
                case "#800000" -> RED_DOWN;
                case "#FFFFFF" -> WHITE;
                case "#808080" -> GRAY;
                default -> INVALID;
            };
    }

    public boolean isBlue() {
        return this == BLUE_DOWN || this == BLUE;
    }

    public boolean isRed() {
        return this == RED_DOWN || this == RED;
    }

    public boolean isWhite() {
        return StringUtils.equals(rgbColor, "#FFFFFF");
    }

    public boolean isGray() {
        return StringUtils.equals(rgbColor, "#808080");
    }

    public Color flip() {
        return switch(this) {
            case RED_DOWN -> Color.RED;
            case RED -> Color.RED_DOWN;
            case BLUE_DOWN -> Color.BLUE;
            case BLUE -> Color.BLUE_DOWN;
            default -> this;
        };
    }

    public Color opposite() {
        return switch(this) {
            case RED_DOWN -> Color.BLUE_DOWN;
            case RED -> Color.BLUE;
            case BLUE_DOWN -> Color.RED_DOWN;
            case BLUE -> Color.RED;
            default -> this;
        };
    }
}