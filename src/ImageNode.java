import javafx.scene.image.Image;

public class ImageNode {

    private String imagePath;
    private Image image;

    public ImageNode(){
        this.imagePath = null;
        this.image = null;
    }

    public ImageNode(String imagePath, Image image){
        this.imagePath = imagePath;
        this.image = image;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
