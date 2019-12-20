package Controller;
/**
 * This class created to handle the logic when user interact with lesion search UI (screen 17)
 *
 * @author s3634096
 *
 */

import Implementation.HttpImpl;
import Implementation.LesionImpl;
import Model.*;
import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.net.URL;
import java.util.*;

public class LesionSearchController implements Initializable {

    // Initialize UI component
    @FXML
    Label hosNameLab;
    @FXML
    Label backLab;
    @FXML
    Label accLab;
    @FXML
    TextField kwField;
    @FXML
    ComboBox<String> typeBox;
    @FXML
    Button manualBtn;
    @FXML
    Button searchBtn;

    // Initialize required variables
    private ScreenController screenController = new ScreenController();
    private LesionResultController lesionResultController = new LesionResultController();
    private HttpImpl httpImpl = new HttpImpl();
    private LesionImpl lesionImpl = new LesionImpl();
    static Map<Long, Lesion> lesionMap = new HashMap<>();


    /**Name: initialize
     **Event: When the lesionsSearch.fxml is loaded
     **Purpose: Setting UI component before showing to user
     **Passed: All UI components named correctly
     **Returns: void
     **Input: void // Output: The lesion search  UI -  the lesionsSearch.fxml is loaded successfully
     **Effect: The method help user to additionally config UI
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set hover when moving mouse to specific label
        accLab.setCursor(Cursor.HAND);
        backLab.setCursor(Cursor.HAND);
        // Set value for comboBox
        typeBox.getItems().addAll("Lesion ID","Diagnoses", "Lesion Type", "Size", "Color");
        typeBox.setValue("Lesion ID");

        // access other
        accLab.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                screenController.closeScreen((Stage) accLab.getScene().getWindow());
                screenController.openScreen("map");
            }
        });

        // back to internal
        backLab.setCursor(Cursor.HAND);
        backLab.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                screenController.closeScreen((Stage) backLab.getScene().getWindow());
                screenController.openScreen("patientRecord");

            }
        });
    }

    public void manualClicked(ActionEvent actionEvent) {
        screenController.closeScreen((Stage) manualBtn.getScene().getWindow());
        screenController.openScreen("lesionsList");
        System.out.println("manual clicked");
    }


    public void searchClicked(ActionEvent actionEvent) {

        if(! kwField.getText().equals("") && !typeBox.getSelectionModel().getSelectedItem().equals("")) {
            lesionResultController.getSearchInfo(kwField.getText(), typeBox.getSelectionModel().getSelectedItem());
            screenController.closeScreen((Stage) searchBtn.getScene().getWindow());
            screenController.openScreen("lesionsResult");
        }
        System.out.println("search clicked");
    }


    public void backClicked(MouseEvent mouseEvent) {
        System.out.println("back clicked");
    }

    public void accClicked(MouseEvent mouseEvent) {
        System.out.println("access  clicked");
    }

    public void logClicked(MouseEvent mouseEvent) {
        screenController.closeScreen((Stage) hosNameLab.getScene().getWindow());
        screenController.openScreen("login");
    }









}
