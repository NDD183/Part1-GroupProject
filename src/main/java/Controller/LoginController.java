package Controller;

import Implementation.ClinicImpl;
import Implementation.HttpImpl;
import Model.Clinic;
import Model.Patient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

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

    static Map<Long, Clinic> clinicMap =  new HashMap<>();
    private ScreenController screenController = new ScreenController();
    private RecordController recordController = new RecordController();
    private HttpImpl httpImpl = new HttpImpl();
    private ClinicImpl clinicImpl = new ClinicImpl();
    private ArrayList<String> cnameList = new ArrayList<>();


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
        String type = "INTERNAL";

        // Set Text for Label component in UI
        typeLab.setText(type+" Log in");
        ruleLab.setText("Please use your hospital user name and password to log in the VisiDerm System");
        errorArea.setVisible(false);
        errorArea.setText("");
        // Configure server combobox value
        serverBox.getItems().addAll("Dermatologist","Head Doctor");
        serverBox.setValue("Dermatologist");

        // Get current available hospital list
        // Load data to map collection
        loadData(httpImpl.fetchGetRequest("http://visiderm.herokuapp.com/api/v1/clinics"));
        // Insert clinic names to comboBox UI component
        insertComboBox();
    }

    // ALL EVENT FUNCTION


    /**Name: initialize
     **Event: When the patientRecord.fxml is loaded
     **Purpose: Setting UI component before showing to user
     **Passed: All UI components named correctly
     **Returns: void
     **Input: void // Output: The record  UI -  the patientRecord.fxml is loaded successfully
     **Effect: The method help user to additionally config UI
     */
    public void loginPressed(ActionEvent actionEvent) {
//        unameField.setText("admin");
//        pwdField.setText("admin");
        if(unameField.getText().equals("admin") && (pwdField.getText().equals("admin")) && serverBox.getSelectionModel().getSelectedItem().equals("Head Doctor")) {
            String[] clinicInfo = hosBox.getSelectionModel().getSelectedItem().split(" - ");
            System.out.println(clinicInfo[0]);
            recordController.getClinicID(clinicInfo[0]);
            recordController.getServer("Head Doctor");
            screenController.closeScreen((Stage) logBtn.getScene().getWindow());
            screenController.openScreen("patientRecord");
        } else if(unameField.getText().equals("user") && (pwdField.getText().equals("user")) && serverBox.getSelectionModel().getSelectedItem().equals("Dermatologist")) {
            String[] clinicInfo = hosBox.getSelectionModel().getSelectedItem().split(" - ");
            System.out.println(clinicInfo[0]);
            recordController.getClinicID(clinicInfo[0]);
            recordController.getServer("Dermatologist");
            screenController.closeScreen((Stage) logBtn.getScene().getWindow());
            screenController.openScreen("patientRecord");
        } else {
            errorArea.setVisible(true);
            errorArea.setText("The user name cannot be found in this server.\nPlease check your information and try again!");
            errorArea.setStyle("-fx-text-fill: red");

        }

    }
    /**Name: initialize
     **Event: When the patientRecord.fxml is loaded
     **Purpose: Setting UI component before showing to user
     **Passed: All UI components named correctly
     **Returns: void
     **Input: void // Output: The record  UI -  the patientRecord.fxml is loaded successfully
     **Effect: The method help user to additionally config UI
     */
    public void onEnter(ActionEvent actionEvent) {
        System.out.println("test") ;
    }


    // ALL SUPPORT FUNCTIONS

    public void insertComboBox(){

        cnameList = clinicImpl.getClinicNameID(clinicMap);
        if(!cnameList.isEmpty()) {
            hosBox.setItems(FXCollections.observableArrayList(cnameList));
            hosBox.setValue(cnameList.get(0));
            String [] hosnameID = cnameList.get(0).split(" - ");
            hosNameLab.setText(hosnameID[1] .toUpperCase()+ " HOSPITAL");
        } else {
            hosBox.setValue("No available hospital");
            hosNameLab.setText("INVALID HOSPITAL");
        }
    }


    /**Name: initialize
     **Event: When the patientRecord.fxml is loaded
     **Purpose: Setting UI component before showing to user
     **Passed: All UI components named correctly
     **Returns: void
     **Input: void // Output: The record  UI -  the patientRecord.fxml is loaded successfully
     **Effect: The method help user to additionally config UI
     */

    public void loadData(String response) {

        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        JsonArray contentResponse = (JsonArray) jsonObject.get("content");

        for (int i = 0; i < contentResponse.size(); i++) {
            String[] infos = httpImpl.exactHttpResponse("clinic", contentResponse.get(i).getAsJsonObject());
                long cid;
                try {
                    cid = Long.parseLong(infos[0]);
                } catch (NumberFormatException nfe) {
                    System.out.println(nfe.toString());
                    break;
                }
            addClinic(cid, infos[1] , infos[2], infos[3]);
            }
        }

    /**Name: addPatient
     ** Purpose: This method helps to add staff to system from the provided file (execute in first run of application only)
     ** @return String - contains "OK": successfully add staff info
     */
    public String addClinic(long id, String name, String city, String country){

        // Create new visit model
        Clinic clinic = new Clinic(id, city, country, name);

//        clinic.setId(id);
//        clinic.setCity(city);
//        clinic.setCountry(country);
//        clinic.setName(name);

        if (clinicMap == null) {
            clinicMap = clinicImpl.addClinic(clinic, new HashMap<>());
        } else {
            clinicMap = clinicImpl.addClinic(clinic, clinicMap);
        }
        return "OK";
    }

}
