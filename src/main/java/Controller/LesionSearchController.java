package Controller;
/**
 * This class created to handle the logic when user interact with lesion search UI (screen 17)
 *
 * @author s3634096
 *
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

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
        typeBox.getItems().addAll("Lesion ID","Diagnoses", "Lesion type", "Size", "Colour");
        typeBox.setValue("Lesion Id");
    }

    public void manualClicked(ActionEvent actionEvent) {
        screenController.closeScreen((Stage) manualBtn.getScene().getWindow());
        screenController.openScreen("lesionsList");
        System.out.println("manual clicked");
    }


    public void searchClicked(ActionEvent actionEvent) {
        screenController.closeScreen((Stage) searchBtn.getScene().getWindow());
        screenController.openScreen("lesionsResult");
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
