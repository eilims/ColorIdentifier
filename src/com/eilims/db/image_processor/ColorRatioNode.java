package com.eilims.db.image_processor;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class ColorRatioNode {

    private final int ALPHA_MASK = 0xFF000000;
    private final int RED_MASK = 0x00FF0000;
    private final int GREEN_MASK = 0x0000FF00;
    private final int BLUE_MASK = 0x000000FF;

    private int pixel;
    private int alphaRatio;
    private int redRatio;
    private int greenRatio;
    private int blueRatio;

    public ColorRatioNode() {
        this.pixel = 0;
        this.alphaRatio = 0;
        this.redRatio = 0;
        this.greenRatio = 0;
        this.blueRatio = 0;
    }

    ColorRatioNode(int pixel) {
        this.pixel = pixel;
        this.alphaRatio = (int) (100 * (this.getAlpha(pixel) / this.getAlpha(this.ALPHA_MASK)));
        this.redRatio = (int) (100 * (this.getRed(pixel) / this.getRed(this.RED_MASK)));
        this.greenRatio = (int) (100 * (this.getGreen(pixel) / this.getGreen(this.GREEN_MASK)));
        this.blueRatio = (int) (100 * (this.getBlue(pixel) / this.getBlue(this.BLUE_MASK)));
    }

    //Returns this - colorToCompare
    public int getDistance(ColorRatioNode colorToCompare) {
        return (int) sqrt((2 * pow((this.getRed() - colorToCompare.getRed()), 2))
                + (4 * pow((this.getGreen() - colorToCompare.getGreen()), 2))
                + (3 * pow((this.getBlue() - colorToCompare.getBlue()), 2))
        );
    }

    //Requires additional processing due to an arithmetic shift cause by a present negative value
    // 0xFF000000 >> 24 == -1
    // 0x7F000000 >> 24 == 127
    // (0xFF000000 >> 24) & 0xFF == 255
    public int getAlpha() {
        return (((this.pixel & this.ALPHA_MASK) >> 24) & this.BLUE_MASK);
    }

    public int getRed() {
        return (this.pixel & this.RED_MASK) >> 16;
    }

    public int getGreen() {
        return (this.pixel & this.GREEN_MASK) >> 8;
    }

    public int getBlue() {
        return (this.pixel & this.BLUE_MASK);
    }

    private double getAlpha(int pixel) {
        return (pixel & this.ALPHA_MASK) >> 24;
    }

    private double getRed(int pixel) {
        return (pixel & this.RED_MASK) >> 16;
    }

    private double getGreen(int pixel) {
        return (pixel & this.GREEN_MASK) >> 8;
    }

    private double getBlue(int pixel) {
        return pixel & this.BLUE_MASK;
    }

    public int getPixel() {
        return pixel;
    }

    public void setPixel(int pixel) {
        this.pixel = pixel;
    }

    public int getAlphaRatio() {
        return alphaRatio;
    }

    public void setAlphaRatio(int alphaRatio) {
        this.alphaRatio = alphaRatio;
    }

    public int getRedRatio() {
        return redRatio;
    }

    public void setRedRatio(int redRatio) {
        this.redRatio = redRatio;
    }

    public int getGreenRatio() {
        return greenRatio;
    }

    public void setGreenRatio(int greenRatio) {
        this.greenRatio = greenRatio;
    }

    public int getBlueRatio() {
        return blueRatio;
    }

    public void setBlueRatio(int blueRatio) {
        this.blueRatio = blueRatio;
    }

}
