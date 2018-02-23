package com.eilims.db.image_processor;

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

    //Member Variables
    private ArrayList<ImageNode> imageNodeList;
    private ArrayList<ImageNode> contrastImageList;
    private ArrayList<Color> colorInImage;
    private ImageProcessor imageProcessor;
    private HBox hbox;
    private VBox vbox;

    @Override
    public void start(Stage stage) {

        //com.eilims.db.image_processor.Color array to be used for various color processing specifications
        Color[] colorArray = Color.values();

        //Instantiate array lists
        this.colorInImage = new ArrayList<>();
        this.imageNodeList = new ArrayList<>();
        this.contrastImageList = new ArrayList<>();

        //Populate array lists with image data
        this.contrastImageList = resetImageList();
        this.imageNodeList = resetImageList();

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
            button.setOnAction(event -> buttonAction(button, x));
            buttonArrayList.add(button);
        }

        //Creating reset button
        Button button7 = new Button("Reset");
        button7.setOnAction(event -> {
            for(int i = 0; i < buttonArrayList.size(); i ++) buttonArrayList.get(i).setText("Parse for " + colorArray[i]);
            imageNodeList = resetImageList();
            contrastImageList = resetImageList();
            ArrayList<ArrayList<ImageNode>> imageNodeListList = new ArrayList<>();
            imageNodeListList.add(this.imageNodeList);
            imageNodeListList.add(this.contrastImageList);
            drawImages(imageNodeListList);
            colorInImage = new ArrayList<>();
        });

        //Adding parsing buttons to the vbox
        buttonArrayList.forEach(button -> vbox.getChildren().add(button));
        //Add reset button to the vbox
        vbox.getChildren().add(button7);
        //Add images to hbox
        ArrayList<ArrayList<ImageNode>> imageNodeListList = new ArrayList<>();
        imageNodeListList.add(this.imageNodeList);
        imageNodeListList.add(this.contrastImageList);
        drawImages(imageNodeListList);

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
    private void buttonAction(Button button, Color color) {
        ArrayList<ArrayList<ImageNode>> imageNodeListList = new ArrayList<>();
        if (button.getText().equals("Parse for " + color.getString())) {
            button.setText("Disable " + color + " Parsing");
            colorInImage.add(color);
            this.imageProcessor.processImageColors(this.colorInImage, this.imageNodeList, ImageProcessor.ColorProcessingTypes.BLACKOUT_GENERATOR);
            this.imageProcessor.processImageColors(this.colorInImage, this.contrastImageList, ImageProcessor.ColorProcessingTypes.CONTRAST_GENERATOR);
            imageNodeListList.add(this.imageNodeList);
            imageNodeListList.add(this.contrastImageList);
            drawImages(imageNodeListList);

        } else {
            button.setText("Parse for " + color);
            colorInImage.remove(color);
            this.imageNodeList = resetImageList();
            this.contrastImageList = resetImageList();
            this.imageProcessor.processImageColors(this.colorInImage, this.imageNodeList, ImageProcessor.ColorProcessingTypes.BLACKOUT_GENERATOR);
            this.imageProcessor.processImageColors(this.colorInImage, this.contrastImageList, ImageProcessor.ColorProcessingTypes.CONTRAST_GENERATOR);
            imageNodeListList.add(this.imageNodeList);
            imageNodeListList.add(this.contrastImageList);
            drawImages(imageNodeListList);
        }
    }

    //Redraws all images on screen
    //TODO find way for only one arrayList to be passed in
    private void drawImages(ArrayList<ArrayList<ImageNode>> imageNodeListList) {

        //remove all edited images but save the returned values
        if (hbox.getChildren().size() > 1) {
            hbox.getChildren().remove(1, imageNodeListList.get(0).size() + 1);
        }

        //Create a list of vboxs with an entry for every imageNode in the list
        //The number of images in all lists must be equal
        ArrayList<VBox> vboxList = new ArrayList<>();
        for(ImageNode imageNode : imageNodeListList.get(0)){
            vboxList.add(new VBox());
        }

        //add images to vbox
        imageNodeListList.forEach(imageNodeList -> imageNodeList.forEach(imageNode -> vboxList.get(imageNodeList.indexOf(imageNode)).getChildren().add(new ImageView(imageNode.getImage()))));

        //Add vbox to hbox
        vboxList.forEach(vbox -> hbox.getChildren().add(vbox));

    }

    //Deletes all references to edited images. Creates new references to original image without edits
    private ArrayList<ImageNode> resetImageList(){
        ArrayList<ImageNode> arrayList = new ArrayList<>();
        populateImageList(arrayList, "C:\\Users\\db217620\\IdeaProjects\\ImageTest\\images");
        return arrayList;
    }
}