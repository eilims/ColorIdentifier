package com.eilims.db.image_processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageProcessorTest {

    private ImageProcessor imageProcessor;

    @BeforeEach
    void setUp() {
        imageProcessor = new ImageProcessor();
        Color[] colorArray = Color.values();
        int index = 0;
        for(Color color : colorArray){
            System.out.println("Index: " +  index + "  "+ color.getString() + ": " + ((color.getColorRatioNode().getPixel() & 0x00FFFFFF)));
            index++;
        }
    }

    @Test
    void calculateEdgeMagnitude() {
        int[][] array = {{-1, 1, 1},
                         {2, 1, 1},
                         {2, 1, 1}
                        };
        int[][] array2 = {{-1, 4, 4},
                          {-1, 4, 4},
                          {-1, 4, 4}
                        };
        int result = imageProcessor.calculateEdgeMagnitude(array);
        assertEquals(260355, result);
        System.out.println(imageProcessor.calculateEdgeMagnitude(array2));
        assertEquals(4, imageProcessor.calculateEdgeMagnitude(array2)/(Color.YELLOW.getColorRatioNode().getPixel() & 0x00FFFFFF));
    }
}