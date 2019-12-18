package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
/**
 * This class created to handle the logic when user interact with login UI (screen 5 - 6)
 *
 * @author s3634096
 *
 */
public class LoginController implements Initializable {

    // Initialize UI component
    @FXML
    Label hosNameLab;
    @FXML
    Label typeLab;
    @FXML
    Label ruleLab;
    @FXML
    TextArea errorArea;
    @FXML
    TextField unameField;
    @FXML
    PasswordField pwdField;
    @FXML
    ComboBox<String> serverBox;
    @FXML
    ComboBox<String> hosBox;
    @FXML
    Button logBtn;

    private ScreenController screenController = new ScreenController();

    /**Name: initialize
     **Event: When the login.fxml is loaded
     **Purpose: Setting UI component before showing to user
     **Passed: All UI components named correctly
     **Returns: void
     **Input: void // Output: The Login  UI -  the login.fxml is loaded successfully
     **Effect: The method help user to additionally config UI
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String hosName = "SIR JOSH MONASH";
        String type = "INTERNAL";

        // Set Text for Label component in UI
        hosNameLab.setText(hosName+" HOSPITAL");
        typeLab.setText(type+" Log in");
        ruleLab.setText("Please use your hospital user name and password to log in the VisiDerm System");
        errorArea.setVisible(false);
        errorArea.setText("");
        // Configure server combobox value
        serverBox.getItems().addAll("Dermatologist","Head Doctor");
        serverBox.setValue("Dermatologist");
        // Configure hospital combobox value
        hosBox.getItems().addAll("HAHA","HIHI", "HOHO");
        hosBox.setValue("HAHA");

    }


    public void loginPressed(ActionEvent actionEvent) {
        unameField.setText("admin");
        pwdField.setText("admin");
       // System.out.println("Login pressed");
        if(unameField.getText().equals("admin") && (pwdField.getText().equals("admin"))) {
            screenController.closeScreen((Stage) logBtn.getScene().getWindow());
            screenController.openScreen("patientRecord");
        } else {
            errorArea.setVisible(true);
            errorArea.setText("The user name cannot be found in this server.\nPlease check your information and try again!");
            errorArea.setStyle("-fx-text-fill: red");

        }

    }


    public void onEnter(ActionEvent actionEvent) {
        System.out.println("test") ;

    }


}
