import javafx.scene.image.WritableImage;

public class ImageNode {

    private String imagePath;
    private WritableImage image;

    public ImageNode() {
        this.imagePath = null;
        this.image = null;
    }

    public ImageNode(String imagePath, WritableImage image) {
        this.imagePath = imagePath;
        this.image = image;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public WritableImage getImage() {
        return image;
    }

    public void setImage(WritableImage image) {
        this.image = image;
    }
}
