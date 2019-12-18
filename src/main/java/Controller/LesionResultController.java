package Controller;
/**
 * This class created to handle the logic when user interact with lesion result UI (screen 19)
 *
 * @author s3634096
 *
 */
import Model.Lesion;
import Model.Patient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class LesionResultController implements Initializable {

    // Initialize UI component
    @FXML
    Label hosNameLab;
    @FXML
    Label backLab;
    @FXML
    Label accLab;
    @FXML
    Label dateLab;
    @FXML
    Label timeLab;
    @FXML
    Label sizeLab;
    @FXML
    Label lidLab;
    @FXML
    Label foundLab;
    @FXML
    TableView<Lesion> lesionTable;
    @FXML
     TableColumn<Lesion, String> lidColumn;
    @FXML
     TableColumn<Lesion, String> docColumn;
    @FXML
     TableColumn<Lesion, String> dateColumn;
    @FXML
     TableColumn<Lesion, String> timeColumn;
    @FXML
     TableColumn<Lesion, Integer> hosColumn;
    @FXML
     TableColumn<Lesion, String> diagColumn;
    @FXML
     TableColumn<Lesion, ImageView> imgColumn;
    @FXML
    Pagination lesionPage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set hover when moving mouse to specific label
        accLab.setCursor(Cursor.HAND);
        backLab.setCursor(Cursor.HAND);
    }

    public void backClicked(MouseEvent mouseEvent) {
        System.out.println("back clicked");
    }

    public void accClicked(MouseEvent mouseEvent) {
        System.out.println("access  clicked");
    }


}
