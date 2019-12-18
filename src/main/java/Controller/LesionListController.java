package Controller;
/**
 * This class created to handle the logic when user interact with lesion browsing UI (screen 18)
 *
 * @author s3634096
 *
 */
import Model.Lesion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LesionListController implements Initializable {


    // Initialize UI component
    @FXML
    Label hosNameLab;
    @FXML
    Label backLab;
    @FXML
    Label accLab;
    @FXML
    TableView<Lesion> lesionTable;
    @FXML
    private TableColumn<Lesion, String> dateColumn;
    @FXML
    private TableColumn<Lesion, String> sizeColumn;
    @FXML
    private TableColumn<Lesion, ImageView> imageColumn;
    @FXML
    Pagination lesionPage;

    private ScreenController screenController = new ScreenController();
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
        System.out.println("acc clicked");
    }

    public void logClicked(MouseEvent mouseEvent) {
        screenController.closeScreen((Stage) hosNameLab.getScene().getWindow());
        screenController.openScreen("login");
    }

}
