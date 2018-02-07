import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;

public class ImageProcessor {

    public ImageProcessor(){}

    public void countRGB(ArrayList<ImageNode> imageList){
        int R = 255 << 16;
        int G = 255 << 8;
        int B = 255;

        imageList.forEach(imageNode -> {
            //pixel counts
            int red = 0;
            int green = 0;
            int blue = 0;
            //Parsing
            int height = (int) imageNode.getImage().getHeight();
            int width = (int) imageNode.getImage().getWidth();
            for (int row = 0; row < height; row++) {
                for (int column = 0; column < width; column++) {
                    int RGB = imageNode.getImage().getPixelReader().getArgb(row, column);
                    if ((RGB & R) != 0) {
                        red++;
                    } else if ((RGB & G) != 0) {
                        green++;
                        imageNode.getImage().getPixelWriter().setArgb(row, column, 0xFF000000);
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
}
