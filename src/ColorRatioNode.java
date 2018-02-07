import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class ColorRatioNode {

    final int ALPHA_MASK = 0xFF000000;
    final int RED_MASK = 0x00FF0000;
    final int GREEN_MASK = 0x0000FF00;
    final int BLUE_MASK = 0x000000FF;

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

    public ColorRatioNode(int pixel) {
        this.pixel = pixel;
        this.alphaRatio = (int) 100 * (this.getAlpha(pixel) / this.getAlpha(this.ALPHA_MASK));
        this.redRatio = (int) 100 * (this.getRed(pixel) / this.getRed(this.RED_MASK));
        this.greenRatio = (int) 100 * (this.getGreen(pixel) / this.getGreen(this.GREEN_MASK));
        this.blueRatio = (int) 100 * (this.getBlue(pixel) / this.getBlue(this.BLUE_MASK));
    }

    //Returns color1 - color2
    public static int getDistance(ColorRatioNode color1, ColorRatioNode color2) {
        return (int) sqrt(pow((color1.getRed() - color2.getRed()), 2)
                        + pow((color1.getGreen() - color2.getGreen()), 2)
                        + pow((color1.getBlue() - color2.getBlue()), 2)
        );
    }

    public int getAlpha() {
        return (this.pixel & this.ALPHA_MASK) >> 24;
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

    public int getAlpha(int pixel) {
        return (pixel & this.ALPHA_MASK) >> 24;
    }

    public int getRed(int pixel) {
        return (pixel & this.RED_MASK) >> 16;
    }

    public int getGreen(int pixel) {
        return (pixel & this.GREEN_MASK) >> 8;
    }

    public int getBlue(int pixel) {
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
