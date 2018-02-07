import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;


public class ImageDisplay extends Application{

    private ArrayList<ImageNode> imageNodeList;

    @Override
    public void start(Stage stage){



        //Instantiate array list and populate it with generated images
        this.imageNodeList = new ArrayList<ImageNode>();
        populateImageList("C:\\Users\\db217620\\IdeaProjects\\ImageTest\\images");

        ImageProcessor imageProcessor = new ImageProcessor();
        imageProcessor.countRGB(this.imageNodeList);

        //All images go into this box
        HBox box = new HBox();
        this.imageNodeList.forEach(imageNode -> {
            //ImageView will display the image passed in as a parameter
            ImageView imageView = new ImageView(imageNode.getImage());
            //ImageView is not a subclass of scene.parent therefore it must be wrapped before being put into a scene
            box.getChildren().add(imageView);
        });

        //Added box to scene
        Scene scene = new Scene(
                box,
                1500,
                1300
        );
        stage.setScene(scene);

        stage.show();

    }

    public static void main(String[] args){

        Application.launch(args);
    }

    //This method will populate the imageNodeList with all files located in the image directory
    public void populateImageList(String imageDirectoryPath) {

        File imageDirectory = new File(imageDirectoryPath);
        try {
            if (imageDirectory.exists()) {
                File[] imagePath = imageDirectory.listFiles();
                for (int i = 0; i < imagePath.length; i++) {
                    Image image = new Image("file:///" + imagePath[i].getPath());
                    imageNodeList.add(new ImageNode(
                            imagePath[i].getPath(),
                            new WritableImage(image.getPixelReader(), (int)image.getWidth(), (int)image.getHeight()))
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Error" + e);
        }
    }

}