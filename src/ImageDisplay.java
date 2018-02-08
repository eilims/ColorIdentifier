import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
      imageProcessor.countRGBDifference(this.imageNodeList);

        //All images go into this hbox
        VBox vbox = new VBox();
        HBox hbox = new HBox(vbox);
        Button button1 = new Button("Parse for Red");
        Button button2 = new Button("Parse for Green");
        Button button3 = new Button("Parse for Blue");
        Button button4 = new Button("Parse for Magenta");
        Button button5 = new Button("Parse for Yellow");
        Button button6 = new Button("Parse for Cyan");
        vbox.getChildren().add(button1);
        vbox.getChildren().add(button2);
        vbox.getChildren().add(button3);
        vbox.getChildren().add(button4);
        vbox.getChildren().add(button5);
        vbox.getChildren().add(button6);
        this.imageNodeList.forEach(imageNode -> {
            //ImageView will display the image passed in as a parameter
            ImageView imageView = new ImageView(imageNode.getImage());
            //ImageView is not a subclass of scene.parent therefore it must be wrapped before being put into a scene
            hbox.getChildren().add(imageView);
        });

        //Added hbox to scene
        Scene scene = new Scene(hbox,1500,1300);
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