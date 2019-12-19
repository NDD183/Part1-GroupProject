package Model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Date;

public class LesionHistory {

    private Long id;

    private String date;

    private String note;

    private String size;

    private String type;

    private String diagnoses;

    private String color;

    private String time;

    private ImageView imageView;

    public LesionHistory(Long id, String date, String note, String size, String type, String diagnoses, String color, String time, ImageView imageView) {
        this.id = id;
        this.date = date;
        this.note = note;
        this.size = size;
        this.type = type;
        this.diagnoses = diagnoses;
        this.color = color;
        this.time = time;
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(String diagnoses) {
        this.diagnoses = diagnoses;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSize() {
        return size + " cm";
    }

    public void setSize(String size) {
        this.size = size;
    }
}
