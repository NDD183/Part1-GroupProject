package Model;

import javafx.scene.image.ImageView;

public class LesionImage {


    private String imageName;
    private ImageView image;

    public LesionImage(String imageName, ImageView img) {
        this.imageName = imageName;
        this.image = img;
    }

    public void setImage(ImageView value) {
        image = value;
    }

    public ImageView getImage() {
        return image;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

}
