import java.lang.reflect.Array;
import java.util.ArrayList;

public class ImageProcessor {

    private final String[] colorNames = {"Red", "Green", "Blue", "Yellow", "Magenta", "Cyan"};

    private boolean[] colorParsing = new boolean[colorNames.length];;

    private final ColorRatioNode red = new ColorRatioNode(0xFFFF0000);
    private final ColorRatioNode green = new ColorRatioNode(0xFF00FF00);
    private final  ColorRatioNode blue = new ColorRatioNode(0xFF0000FF);
    private final ColorRatioNode yellow = new ColorRatioNode(0xFFFFFF00);
    private final ColorRatioNode magenta = new ColorRatioNode(0xFFFF00FF);
    private final ColorRatioNode cyan = new ColorRatioNode(0xFF00FFFF);
    private final ColorRatioNode[] colorValues = {red, green, blue, yellow, magenta, cyan};;

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
        imageList.forEach(imageNode -> {
            //pixel counts
            int[] colorCount = new int[this.colorValues.length];

            //Parsing through each pixel
            int height = (int) imageNode.getImage().getHeight();
            int width = (int) imageNode.getImage().getWidth();
            for (int col = 0; col < width; col++) {
                for (int row = 0; row < height; row++) {
                    ColorRatioNode currentPixel = new ColorRatioNode(imageNode.getImage().getPixelReader().getArgb(col, row));
                    //Find which category the pixel falls into
                    int minimumDistance = Integer.MAX_VALUE;
                    int colorIndex = 0;
                    int[] colorRatio = new int[this.colorValues.length];
                    for (int u = 0; u < this.colorValues.length; u++) {
                        int difference = currentPixel.getDistance(this.colorValues[u]);
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
                System.out.println("Color: " + this.colorNames[i] + "     Value: " + colorCount[i]);
            }
        });
    }

    public void blackoutColor(ArrayList<String> colorList, ArrayList<ImageNode> imageList) {
        //Determine which colors need to be parsed
        for (int i = 0; i < this.colorNames.length; i++) {
            if (colorList.contains(this.colorNames[i])) {
                this.colorParsing[i] = true;
            } else {
                this.colorParsing[i] = false;
            }
        }

        //Parse through each image
        imageList.forEach(imageNode -> {
            //Parsing through each pixel
            int height = (int) imageNode.getImage().getHeight();
            int width = (int) imageNode.getImage().getWidth();
            for (int col = 0; col < width; col++) {
                for (int row = 0; row < height; row++) {
                    ColorRatioNode currentPixel = new ColorRatioNode(imageNode.getImage().getPixelReader().getArgb(col, row));
                    //Find which category the pixel falls into
                    int minimumDistance = Integer.MAX_VALUE;
                    int colorIndex = 0;
                    for (int u = 0; u < this.colorNames.length; u++) {
                        int difference = currentPixel.getDistance(this.colorValues[u]);
                        if (difference < minimumDistance) {
                            colorIndex = u;
                            minimumDistance = difference;
                        }
                    }
                    if(this.colorParsing[colorIndex]){
                        imageNode.getImage().getPixelWriter().setArgb(col, row, 0xFF000000);
                    }

                }
            }

        });

    }
}
