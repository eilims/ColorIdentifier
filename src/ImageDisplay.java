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


public class ImageDisplay extends Application {

    private ArrayList<ImageNode> imageNodeList;
    private ArrayList<ImageNode> contrastImageList;
    private ArrayList<String> colorInImage;
    private ImageProcessor imageProcessor;

    private HBox hbox;
    private VBox vbox;

    @Override
    public void start(Stage stage) {

        //Color array to be used for various color processing specifications
        Color[] colorArray = Color.values();

        //Instantiate array lists
        this.colorInImage = new ArrayList<>();
        this.imageNodeList = new ArrayList<>();
        this.contrastImageList = new ArrayList<>();

        //Populate array lists with image data
        this.contrastImageList = resetImage();
        this.imageNodeList = resetImage();

        //Instantiate image Processor
        this.imageProcessor = new ImageProcessor();

        //Creating GUI layout
        //All buttons go into this vbox
        this.vbox = new VBox();

        //adding vbox into hbox ahead of time
        //VerticalBox goes into HorizontalBox. Like a multidimensional array.
        this.hbox = new HBox(vbox);

        //Button declarations
        //Add all non reset buttons to arrayList for easy processing
        ArrayList<Button> buttonArrayList = new ArrayList<>();
        //For every color in colorArray create a new Parse for ____ button
        for(Color x : colorArray){
            Button button = new Button("Parse for " + x.getString());
            button.setOnAction(event -> buttonAction(button, x.getString()));
            buttonArrayList.add(button);
        }

        //Creating reset button
        Button button7 = new Button("Reset");
        button7.setOnAction(event -> {
            for(int i = 0; i < buttonArrayList.size(); i ++) buttonArrayList.get(i).setText("Parse for " + colorArray[i].getString());
            imageNodeList = resetImage();
            drawImages(imageNodeList, contrastImageList);
            colorInImage = new ArrayList<>();
        });

        //Adding parsing buttons to the vbox
        buttonArrayList.forEach(button -> vbox.getChildren().add(button));
        //Add reset button to the vbox
        vbox.getChildren().add(button7);
        //Add images to hbox
        drawImages(this.imageNodeList, this.contrastImageList);

        //Added hbox to scene
        Scene scene = new Scene(hbox, 1800, 1200);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    //This method will populate the imageNodeList with all files located in the image directory
    private void populateImageList(ArrayList<ImageNode> arrayList, String imageDirectoryPath) {
        File imageDirectory = new File(imageDirectoryPath);
        try {
            if (imageDirectory.exists()) {
                File[] imagePath = imageDirectory.listFiles();
                assert imagePath != null;
                for (File anImagePath : imagePath) {
                    Image image = new Image("file:///" + anImagePath.getPath());
                    arrayList.add(new ImageNode(
                            anImagePath.getPath(),
                            new WritableImage(image.getPixelReader(),
                                    (int) image.getWidth(),
                                    (int) image.getHeight()))
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Error" + e);
        }
    }

    //Resets button text and activate iamage parsing
    private void buttonAction(Button button, String color) {
        if (button.getText().equals("Parse for " + color)) {
            button.setText("Disable " + color + " Parsing");
            colorInImage.add(color);
            this.imageProcessor.processImageColors(this.colorInImage, this.imageNodeList, ImageProcessor.ColorProcessingTypes.BLACKOUT);
            this.imageProcessor.processImageColors(this.colorInImage, this.contrastImageList, ImageProcessor.ColorProcessingTypes.CONTRAST_GENERATOR);
            drawImages(this.imageNodeList, this.contrastImageList);

        } else {
            button.setText("Parse for " + color);
            colorInImage.remove(color);
            this.imageNodeList = resetImage();
            this.imageProcessor.processImageColors(this.colorInImage, this.imageNodeList, ImageProcessor.ColorProcessingTypes.BLACKOUT);
            this.imageProcessor.processImageColors(this.colorInImage, this.contrastImageList, ImageProcessor.ColorProcessingTypes.CONTRAST_GENERATOR);
            drawImages(this.imageNodeList, this.contrastImageList);
        }
    }

    //Redraws all images on screen
    //TODO find way for only one method to be apparent
    private void drawImages(ArrayList<ImageNode> array, ArrayList<ImageNode> contrastArray) {
        //remove all edited images but save the returned values
        if (hbox.getChildren().size() > 1) {
            hbox.getChildren().remove(1, array.size() + 1);
        }
        //Add original unaltered images back
        for(int i = 0; i < array.size(); i++){
            this.hbox.getChildren().add(new VBox(new ImageView(array.get(i).getImage()), new ImageView(contrastArray.get(i).getImage())));
        }
    }

    //Deletes all references to edited images. Creates new references to original image without edits
    private ArrayList<ImageNode> resetImage(){
        ArrayList<ImageNode> arrayList = new ArrayList<>();
        populateImageList(arrayList, "C:\\Users\\db217620\\IdeaProjects\\ImageTest\\images");
        return arrayList;
    }
}