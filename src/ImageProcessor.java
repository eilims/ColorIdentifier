import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class ImageProcessor {

    public ImageProcessor() {
    }

    public void countRGB(ArrayList<ImageNode> imageList) {
        int R = 255 << 16;
        int G = 255 << 8;
        int B = 255;

        imageList.forEach(imageNode -> System.out.println("Hello!"));


        imageList.forEach(imageNode -> {
            //pixel counts
            int red = 0;
            int green = 0;
            int blue = 0;
            //Parsing
            int height = (int) imageNode.getImage().getHeight();
            int width = (int) imageNode.getImage().getWidth();
            for (int col = 0; col < width; col++) {
                for (int row = 0; row < height; row++) {
                    int RGB = imageNode.getImage().getPixelReader().getArgb(col, row);
                    if ((RGB & R) != 0) {
                        red++;
                    } else if ((RGB & G) != 0) {
                        green++;
                        //imageNode.getImage().getPixelWriter().setArgb(col, row, 0xFF000000);
                    } else if ((RGB & B) != 0) {
                        blue++;
                    }
                }
            }
            System.out.println("Image: " + imageNode.getImagePath());
            System.out.println("Red Pixels: " + red);
            System.out.println("Green pixels: " + green);
            System.out.println("Blue Pixels: " + blue);
        });
    }

    public void countRGBDifference(ArrayList<ImageNode> imageList) {
        //RGB values for color. exclude alpha for ease of use
        ColorRatioNode red = new ColorRatioNode(0xFF0000);
        ColorRatioNode green = new ColorRatioNode(0x00FF00);
        ColorRatioNode blue = new ColorRatioNode(0x0000FF);
        ColorRatioNode yellow = new ColorRatioNode(0xFFFF00);
        ColorRatioNode magenta = new ColorRatioNode(0xFF00FF);
        ColorRatioNode[] colorValues = {red, green, blue, yellow, magenta};

        imageList.forEach(imageNode -> {
            //pixel counts
            int[] colorCount = new int[colorValues.length];

            //Parsing through each pixel
            int height = (int) imageNode.getImage().getHeight();
            int width = (int) imageNode.getImage().getWidth();
            for (int col = 0; col < width; col++) {
                for (int row = 0; row < height; row++) {
                    ColorRatioNode currentPixel = new ColorRatioNode(
                            imageNode.getImage().getPixelReader().getArgb(col, row)
                    );
                    //Find which category the pixel falls into
                    int minimumDistance = Integer.MAX_VALUE;
                    int colorIndex = 0;
                    int[] colorRatio = new int[colorValues.length];
                    for (int u = 0; u < colorValues.length; u++) {
                        int difference = ColorRatioNode.getDistance(currentPixel, colorValues[u]);
                        if (difference < minimumDistance) {
                            colorIndex = u;
                            minimumDistance = difference;
                        }
                    }
                    colorCount[colorIndex]++;
                }
            }
            System.out.println("Image: " + imageNode.getImagePath());
            for (int i = 0; i < colorCount.length; i++) {
                System.out.println("Index " + i + " Value: " + colorCount[i]);
            }
        });
    }
}
