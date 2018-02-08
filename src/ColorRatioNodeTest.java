import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorRatioNodeTest {

    ColorRatioNode color1;
    ColorRatioNode color2;
    ColorRatioNode color3;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        //Magenta
        color1 = new ColorRatioNode(0xFFFF00FF);
        //Yellow
        color2 = new ColorRatioNode(0xFFFFFF00);
        //Cyan?
        color3 = new ColorRatioNode(0xFFA8D3FF);
    }

    @org.junit.jupiter.api.Test
    void getDistance() {
        //magenta vs magenta
        assertEquals(0, color1.getDistance(color1));
        //magenta bs yellow
        assertEquals(674, color1.getDistance(color2));
        //Cyan? vs blue
        assertEquals(484, color3.getDistance(new ColorRatioNode(0xFF0000FF)));
        //cyan vs magenta
        assertEquals(439, color3.getDistance(color1));
    }

    @org.junit.jupiter.api.Test
    void getAlpha() {
        assertEquals(0xFF, color1.getAlpha());
        assertEquals(0xFF, color2.getAlpha());
        assertEquals(0xFF, color3.getAlpha());
    }

    @org.junit.jupiter.api.Test
    void getRed() {
        assertEquals(0xFF, color1.getRed());
        assertEquals(0xFF, color2.getRed());
        assertEquals(0xA8, color3.getRed());
    }

    @org.junit.jupiter.api.Test
    void getGreen() {
        assertEquals(0x00, color1.getGreen());
        assertEquals(0xFF, color2.getGreen());
        assertEquals(0xD3, color3.getGreen());
    }

    @org.junit.jupiter.api.Test
    void getBlue() {
        assertEquals(0xFF, color1.getBlue());
        assertEquals(0x00, color2.getBlue());
        assertEquals(0xFF, color3.getBlue());
    }

    @Test
    void getAlphaRatio() {
        assertEquals(100, color1.getAlphaRatio());
        assertEquals(100, color2.getAlphaRatio());
        assertEquals(100, color3.getAlphaRatio());
    }

    @Test
    void getRedRatio() {
        assertEquals(100, color1.getRedRatio());
        assertEquals(100, color2.getRedRatio());
        assertEquals(65, color3.getRedRatio());
    }

    @Test
    void getGreenRatio() {
        assertEquals(0, color1.getGreenRatio());
        assertEquals(100, color2.getGreenRatio());
        assertEquals(82, color3.getGreenRatio());
    }

    @Test
    void getBlueRatio() {
        assertEquals(100, color1.getBlueRatio());
        assertEquals(0, color2.getBlueRatio());
        assertEquals(100, color3.getBlueRatio());
    }

}