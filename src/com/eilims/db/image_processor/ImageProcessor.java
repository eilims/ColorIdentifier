package com.eilims.db.image_processor;

import java.util.ArrayList;

public class ImageProcessor {

    //Member Variables
    //Get colors array
    private Color[] colors = Color.values();
    //Set int for ease of reading
    private int numberOfColors = colors.length;
    //Boolean array for parsing selected colors for image processing
    private boolean[] colorParsing;


    //Enum for all processing types
    public enum ColorProcessingTypes {
        BLACKOUT_GENERATOR, //Turns all selected colors black
        CONTRAST_GENERATOR, //Deprecated do not use. Turns all seleccted colors white, and other colors black
        EDGE_GENERATOR_PREPROCESS, //Creates an edge around each selected color. Process the color of each pixel before drawing the edge
        EDGE_GENERATOR //Creates an edge around each selected color. Process the color while drawing the edges
    }


    //Constructors
    ImageProcessor() {
        //Generate colorParsing array based on com.eilims.db.image_processor.Color enum
        colorParsing = new boolean[this.numberOfColors];
    }


    //Methods
    //Check which colors are currently selected
    private void parseForColors(ArrayList<Color> colorList) {
        for (int i = 0; i < this.numberOfColors; i++)
            this.colorParsing[i] = colorList.contains(this.colors[i]);
    }


    //Calculates which color the currentPixel is maps to.
    //Returns -1 if the color is not mapped to any color
    private int getColorIndex(ColorRatioNode currentPixel) {

        int minimumDistance = Integer.MAX_VALUE;
        int colorIndex = -1;

        for (int u = 0; u < this.numberOfColors; u++) {
            int difference = currentPixel.getDistance(this.colors[u].getColorRatioNode());
            if (difference < minimumDistance) {
                colorIndex = u;
                minimumDistance = difference;
            }
        }
        return colorIndex;
    }


    //TODO do not use contrast generator
    //TODO implement color edge detection variant color calc before calculation and color calc during calculation
    //Counts colored pixels and processes the image depending on passed in processingType
    public void processImageColors(ArrayList<Color> colorList, ArrayList<ImageNode> imageNodeList, ColorProcessingTypes processingType) {
        //Determine which colors need to be parsed
        this.parseForColors(colorList);
        switch (processingType) {
            case BLACKOUT_GENERATOR:
                this.blackoutColor(colorList, imageNodeList);
                break;
            case CONTRAST_GENERATOR:
                preprocessEdgeCreator(colorList, imageNodeList);
                break;
            case EDGE_GENERATOR_PREPROCESS:
                preprocessEdgeCreator(colorList, imageNodeList);
                break;
        }

    }


    //Helper method for preprocessing edge detection
    private int[][] preprocessColorData(ImageNode imageNode) {
        //Get image height and width
        int imageHeight = (int) imageNode.getImage().getHeight();
        int imageWidth = (int) imageNode.getImage().getWidth();

        //2D array where all values will be stored
        int[][] imageColorAssignment = new int[imageWidth][imageHeight];

        //Parse through image to associate colors
        for (int col = 0; col < imageWidth; col++) {
            for (int row = 0; row < imageHeight; row++) {
                //Get corresponding color index
                int colorIndex = getColorIndex(new ColorRatioNode(imageNode.getImage().getPixelReader().getArgb(col, row)));
                //If the color is being parsed for allow is to exist else make is -1 which corresponds to black
                if (colorParsing[colorIndex]) {
                    imageColorAssignment[col][row] = colorIndex;
                } else {
                    imageColorAssignment[col][row] = -1;
                }
            }
        }
        return imageColorAssignment;
    }

    //calculates the horizontal and vertical Sobel Operators and returns the magnitude of the two results
    public int calculateEdgeMagnitude(int[][] imageMatrix) {
        //Variable for vertical and horizontal Sobel Operators
        int colorDifferenceVertical = 0;
        int colorDifferenceHorizontal = 0;

        //Sobel Operators
        int[][] sobelVertical = {{1, 0, -1},
                {2, 0, -2},
                {1, 0, -1}
        };
        int[][] sobelHorizontal = {{1, 2, 1},
                {0, 0, 0},
                {-1, -2, -1}
        };

        //Calculate Edge Magnitude
        /*
            All calculations are done in reference to the center node color.
            If the center node is not a color we are parsing for the node is black
         */
        ColorRatioNode centerPixel;
        if (imageMatrix[1][1] != -1) {
            centerPixel = this.colors[imageMatrix[1][1]].getColorRatioNode();
        } else {
            centerPixel = new ColorRatioNode(0xFF000000);
        }

        /*
            Checking each of the pixels in a 3x3 square around the center node
         */
        for (int currentPixelColumn = 0; currentPixelColumn < imageMatrix.length; currentPixelColumn++) {
            for (int currentPixelRow = 0; currentPixelRow < imageMatrix[0].length; currentPixelRow++) {
                //If the pixel is one we are looking for then give is a normal value else consider it black
                //We calculate the distance so that it is "fair" when considering whether the pixel is an edge or not
                int pixelValue;
                if (imageMatrix[currentPixelColumn][currentPixelRow] != -1) {
                    pixelValue = centerPixel.getDistance(this.colors[imageMatrix[currentPixelColumn][currentPixelRow]].getColorRatioNode());
                } else {
                    pixelValue = centerPixel.getDistance(new ColorRatioNode(0xFF000000));
                }
                colorDifferenceVertical += (sobelVertical[currentPixelColumn][currentPixelRow] * pixelValue);
                colorDifferenceHorizontal += (sobelHorizontal[currentPixelColumn][currentPixelRow] * pixelValue);
            }
        }
        return (int) Math.sqrt(Math.pow(colorDifferenceVertical, 2) + Math.pow(colorDifferenceHorizontal, 2));
    }

    private void preprocessEdgeCreator(ArrayList<Color> colorList, ArrayList<ImageNode> imageNodeList) {
        //Parse for what colors we need
        this.parseForColors(colorList);

        imageNodeList.forEach(imageNode -> {
            //Selecting what color the pixels are
            int[][] colorAssignments = preprocessColorData(imageNode);

            int imageHeight = (int) imageNode.getImage().getHeight();
            int imageWidth = (int) imageNode.getImage().getWidth();

            //Processing for the edge
            for (int col = 0; col < imageWidth; col++) {
                for (int row = 0; row < imageHeight; row++) {
                    /*
                        Cases for Sobels parsing
                        1. Column is zero.
                            1a. Row is zero.
                            1b. Row is not an edge.
                            1c. Row is imageHeight minus 1.
                        2. Column is not an edge.
                            2a. Row is zero.
                            2b. Row is not an edge.
                            2c. Row is imageHeight minus 1.
                        3. Column is imageWidth - 1.
                            3a. Row is zero.
                            3b. Row is not an edge.
                            3c. Row is imageHeight - 1.
                     */
                    int colorDifference = 0;

                    //When we are in the first column
                    if (col == 0) {
                        if (row == 0) {
                            colorDifference = calculateEdgeMagnitude(new int[][]{{-1, -1, -1}, {-1, colorAssignments[col][row], colorAssignments[col + 1][row]}, {-1, colorAssignments[col][row + 1], colorAssignments[col + 1][row + 1]}});
                        } else if (row == imageHeight - 1) {
                            colorDifference = calculateEdgeMagnitude(new int[][]{{-1, colorAssignments[col][row - 1], colorAssignments[col + 1][row - 1]}, {-1, colorAssignments[col][row], colorAssignments[col + 1][row]}, {-1, -1, -1}});
                        } else {
                            colorDifference = calculateEdgeMagnitude(new int[][]{{-1, colorAssignments[col][row - 1], colorAssignments[col + 1][row - 1]}, {-1, colorAssignments[col][row], colorAssignments[col + 1][row]}, {-1, colorAssignments[col][row + 1], colorAssignments[col + 1][row + 1]}});
                        }

                        //When we are in the last column
                    } else if (col == imageWidth - 1) {
                        if (row == 0) {
                            colorDifference = calculateEdgeMagnitude(new int[][]{{-1, -1, -1}, {colorAssignments[col - 1][row], colorAssignments[col][row], -1}, {colorAssignments[col - 1][row + 1], colorAssignments[col][row + 1], -1}});
                        } else if (row == imageHeight - 1) {
                            colorDifference = calculateEdgeMagnitude(new int[][]{{colorAssignments[col - 1][row - 1], colorAssignments[col][row - 1], -1}, {colorAssignments[col - 1][row], colorAssignments[col][row], -1}, {-1, -1, -1}});
                        } else {
                            colorDifference = calculateEdgeMagnitude(new int[][]{{colorAssignments[col - 1][row - 1], colorAssignments[col][row - 1], -1}, {colorAssignments[col - 1][row], colorAssignments[col][row], -1}, {colorAssignments[col - 1][row + 1], colorAssignments[col][row + 1], -1}});
                        }

                        //any other column
                    } else {
                        if (row == 0) {
                            colorDifference = calculateEdgeMagnitude(new int[][]{{-1, -1, -1}, {colorAssignments[col - 1][row], colorAssignments[col][row], colorAssignments[col + 1][row]}, {colorAssignments[col - 1][row + 1], colorAssignments[col][row + 1], colorAssignments[col + 1][row + 1]}});
                        } else if (row == imageHeight - 1) {
                            colorDifference = calculateEdgeMagnitude(new int[][]{{colorAssignments[col - 1][row - 1], colorAssignments[col][row - 1], colorAssignments[col + 1][row - 1]}, {colorAssignments[col - 1][row], colorAssignments[col][row], colorAssignments[col + 1][row]}, {-1, -1, -1}});
                        } else {
                            colorDifference = calculateEdgeMagnitude(new int[][]{{colorAssignments[col - 1][row - 1], colorAssignments[col][row - 1], colorAssignments[col + 1][row - 1]}, {colorAssignments[col - 1][row], colorAssignments[col][row], colorAssignments[col + 1][row]}, {colorAssignments[col - 1][row + 1], colorAssignments[col][row + 1], colorAssignments[col][row + 1]}});
                        }
                    }
                    int remainder = colorDifference % 255;
                    colorDifference = colorDifference - remainder;
                    colorDifference = colorDifference << 24;
                    colorDifference = colorDifference | 0x00000000;
                    imageNode.getImage().getPixelWriter().setArgb(col, row, colorDifference);
                }
            }
        });
    }

    //Turns the selected colors in colorList black
    //The key is to pass the original unaltered image every time to ensure proper operation and resetting.
    private void blackoutColor(ArrayList<Color> colorList, ArrayList<ImageNode> imageNodeList) {
        //Determine which colors need to be parsed
        this.parseForColors(colorList);

        //Parse through each image
        imageNodeList.forEach(imageNode -> {
            //Parsing through each pixel
            int height = (int) imageNode.getImage().getHeight();
            int width = (int) imageNode.getImage().getWidth();
            //Going through every column in the image
            for (int col = 0; col < width; col++) {
                //Going through every row in the image
                for (int row = 0; row < height; row++) {
                    ColorRatioNode currentPixel = new ColorRatioNode(imageNode.getImage().getPixelReader().getArgb(col, row));
                    //Get the color this pixel is associated with if any. -1 returned if not associated
                    int colorIndex = getColorIndex(currentPixel);
                    //Change color is the pixel if it is selected
                    if (colorIndex != -1) {
                        if (this.colorParsing[colorIndex]) {
                            imageNode.getImage().getPixelWriter().setArgb(col, row, 0xFF000000);
                        }

                    }
                }
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
    //Generates the contrast image for edge detection
    public void generateContrastImage(ArrayList<Color> colorList, ArrayList<ImageNode> imageNodeList) {
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
                    int colorIndex = getColorIndex(currentPixel);
                    if (colorIndex != -1) {
                        if (this.colorParsing[colorIndex]) {
                            imageNode.getImage().getPixelWriter().setArgb(col, row, 0xFFFFFFFF);
                        } else {
                            imageNode.getImage().getPixelWriter().setArgb(col, row, 0xFF000000);
                        }

                    }
                }
            }

        });

    }
}
