import java.util.ArrayList;

public class ImageProcessor {

    //Member Variables
    //Get colors array
    private Color[] colors = Color.values();
    //Set int for ease of reading
    private int numberOfColors = colors.length;
    //Boolean array for parsing selected colors for image processing
    //private boolean[] colorParsing = new boolean[colorValues.length];
    private boolean[] colorParsing;


    //Enum for all processing types
    public enum ColorProcessingTypes {
        BLACKOUT, CONTRAST_GENERATOR
    }


    ImageProcessor() {
        //Generate colorParsing array based on Color enum
        colorParsing = new boolean[this.numberOfColors];
    }

    private void parseForColors(ArrayList<String> colorList) {
        for (int i = 0; i < this.numberOfColors; i++)
            this.colorParsing[i] = colorList.contains(this.colors[i].getString());
    }


    public void processImageColors(ArrayList<String> colorList, ArrayList<ImageNode> imageNodeList, ColorProcessingTypes processingType) {
        //Determine which colors need to be parsed
        this.parseForColors(colorList);

        //Image processing
        imageNodeList.forEach(imageNode -> {

            //Get height and width of image
            int height = (int) imageNode.getImage().getHeight();
            int width = (int) imageNode.getImage().getWidth();

            //Values for colored pixels
            int[] colorCount = new int[this.numberOfColors];

            //Parsing through each pixel
            for (int col = 0; col < width; col++) {
                for (int row = 0; row < height; row++) {

                    //Get pixel and use ColorRatioNode. Not using standard class Color due to float comparison issues.
                    ColorRatioNode currentPixel = new ColorRatioNode(imageNode.getImage().getPixelReader().getArgb(col, row));

                    //Find which category the pixel falls into
                    int minimumDistance = Integer.MAX_VALUE;
                    int colorIndex = 0;

                    //Find minimum distance for color
                    //Smallest value indicates which color the current pixel shall be associated with
                    for (int u = 0; u < this.numberOfColors; u++) {
                        int difference = currentPixel.getDistance(this.colors[u].getColorRatioNode());
                        if (difference < minimumDistance) {
                            colorIndex = u;
                            minimumDistance = difference;
                        }
                    }

                    //Pixel is now classified as one of the Color enum values

                    //Increment color count for this image by one pixel
                    colorCount[colorIndex]++;

                    /*
                    Two processing modes available
                    BLACKOUT turns all associated pixels for selected colors black in an image
                    ContrastGenerator turns all associated pixels for selected colors white and unassociated pixels black
                    */

                    //BLACKOUT Logic
                    if (processingType == ColorProcessingTypes.BLACKOUT) {
                        if (this.colorParsing[colorIndex]) {
                            imageNode.getImage().getPixelWriter().setArgb(col, row, 0xFF000000);
                        }
                        //ContrastGenerator Logic
                    } else if (processingType == ColorProcessingTypes.CONTRAST_GENERATOR) {
                        //TODO make new writable image for every color put the in order
                        //This is necessary as each individual color must be circled.
                        if (this.colorParsing[colorIndex]) {
                            imageNode.getImage().getPixelWriter().setArgb(col, row, 0xFFFFFFFF);
                        } else {
                            imageNode.getImage().getPixelWriter().setArgb(col, row, 0xFF000000);
                        }
                    }
                }
            }
            //Print out image statistics to console
            System.out.println("Image: " + imageNode.getImagePath());
            for (int i = 0; i < colorCount.length; i++) {
                System.out.println("Color: " + Color.values()[i] + "     Value: " + colorCount[i]);
            }
        });
    }

    @Deprecated
    //Counts the number of R, G, and B pixels in a picture through direct comparison
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

    @Deprecated
    //Turns the selected colors in colorList black
    //The key is to pass the original unaltered image every time to ensure proper operation and resetting.
    public void blackoutColor(ArrayList<String> colorList, ArrayList<ImageNode> imageNodeList) {
        //Determine which colors need to be parsed
        this.parseForColors(colorList);

        //Parse through each image
        imageNodeList.forEach(imageNode -> {
            //Parsing through each pixel
            int height = (int) imageNode.getImage().getHeight();
            int width = (int) imageNode.getImage().getWidth();
            for (int col = 0; col < width; col++) {
                for (int row = 0; row < height; row++) {
                    ColorRatioNode currentPixel = new ColorRatioNode(imageNode.getImage().getPixelReader().getArgb(col, row));
                    //Find which category the pixel falls into
                    int minimumDistance = Integer.MAX_VALUE;
                    int colorIndex = 0;
                    for (int u = 0; u < this.numberOfColors; u++) {
                        int difference = currentPixel.getDistance(this.colors[u].getColorRatioNode());
                        if (difference < minimumDistance) {
                            colorIndex = u;
                            minimumDistance = difference;
                        }
                    }
                    if (this.colorParsing[colorIndex]) {
                        imageNode.getImage().getPixelWriter().setArgb(col, row, 0xFF000000);
                    }

                }
            }

        });

    }


    @Deprecated
    //Generates the contrast image for edge detection
    public void generateContrastImage(ArrayList<String> colorList, ArrayList<ImageNode> imageNodeList) {
        //Determine which colors need to be parsed
        this.parseForColors(colorList);
        imageNodeList.forEach(imageNode -> {
            //Parsing through each pixel
            int height = (int) imageNode.getImage().getHeight();
            int width = (int) imageNode.getImage().getWidth();
            for (int col = 0; col < width; col++) {
                for (int row = 0; row < height; row++) {
                    ColorRatioNode currentPixel = new ColorRatioNode(imageNode.getImage().getPixelReader().getArgb(col, row));
                    //Find which category the pixel falls into
                    int minimumDistance = Integer.MAX_VALUE;
                    int colorIndex = 0;
                    for (int u = 0; u < this.numberOfColors; u++) {
                        int difference = currentPixel.getDistance(this.colors[u].getColorRatioNode());
                        if (difference < minimumDistance) {
                            colorIndex = u;
                            minimumDistance = difference;
                        }
                    }
                    if (this.colorParsing[colorIndex]) {
                        imageNode.getImage().getPixelWriter().setArgb(col, row, 0xFFFFFFFF);
                    } else {
                        imageNode.getImage().getPixelWriter().setArgb(col, row, 0xFF000000);
                    }

                }
            }

        });

    }
}
