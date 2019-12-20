package Controller;

import com.google.gson.JsonObject;
import component.ParseJson;
import component.RequestHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class VisitRecordController implements Initializable {

    @FXML
    private TextField txtDate;
    @FXML
    private TextField txtTime;
    @FXML
    private TextField txtDoctor;
    @FXML
    private TextField txtClinic;
    @FXML
    private TextField txtPatientName;
    @FXML
    private TextField txtPatientId;
    @FXML
    private TextField txtTimeLession;
    @FXML
    private TextField txtLesionId;
    @FXML
    private TextField txtDateLession;
    @FXML
    private TextField txtLocation;
    @FXML
    private TextField txtLesionSize;
    @FXML
    private TextField txtPatientLession;
    @FXML
    private TextField txtNote;
    @FXML
    private Label accLab;
    @FXML
    private Label searchLab;

    private int defaultId = 1;
    String idVisit;
    JsonObject visit;
    public static Integer selectedVisitID;
            
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadVisitRecord();
        ScreenController screenController = new ScreenController();
        accLab.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                screenController.closeScreen((Stage) accLab.getScene().getWindow());
                screenController.openScreen("map");
            }
        });
        searchLab.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                screenController.closeScreen((Stage) searchLab.getScene().getWindow());
                screenController.openScreen("lesionsSearch");
            }
        });
    }
    
    private void loadVisitRecord() {
        String json = RequestHandler.getVisitById(String.valueOf(selectedVisitID));
        visit = ParseJson.getJson(json);
        System.out.println(visit);
        txtDate.setText(visit.get("startDate").getAsString().substring(0, 10));
        txtTime.setText(visit.get("endDate").getAsString().substring(12, 19));
        txtClinic.setText(visit.get("clinic").getAsJsonObject().get("name").getAsString());
        
        JsonObject patient = ParseJson.getByKey(visit, "patient");
        txtPatientName.setText(patient.get("firstName").getAsString() + " " + patient.get("lastName").getAsString());
        txtPatientId.setText(patient.get("id").getAsString());
        
        JsonObject staff = ParseJson.getByKey(visit, "staff");
        txtDoctor.setText(staff.get("name").getAsString());
        idVisit = visit.get("id").getAsString();
        //loadLessionByVisit(visit.get("id").getAsString());
    }
    
    private void loadLessionByVisit() {
//        String json = RequestHandler.getLessionJson(idVisit);
//        System.out.println(json);
//        JsonObject jsonObject = ParseJson.getJson(json);
//        JsonObject visit = ParseJson.getByKey(jsonObject, "visit");
//        JsonObject patient = ParseJson.getByKey(visit, "patient");
//        txtDateLession.setText(visit.get("startDate").getAsString().substring(0, 10));
//        txtTimeLession.setText(visit.get("startDate").getAsString().substring(12, 19));
//        txtClinicLession.setText(visit.get("clinic").getAsJsonObject().get("name").getAsString());
//        txtDoctorLession.setText(visit.get("staff").getAsJsonObject().get("name").getAsString());
//        txtPatientLession.setText(patient.get("firstName").getAsString() + " " + patient.get("lastName").getAsString());
//        txtNote.setText(visit.get("note").getAsString());
    }
    
    @FXML
    public void updateRecord(ActionEvent e) {
        JsonObject clinic = visit.get("clinic").getAsJsonObject();
        clinic.remove("name");
        clinic.addProperty("name", txtClinic.getText());
        
        visit.remove("startDate");
        visit.addProperty("startDate", txtDate.getText() + "T" + txtTime.getText() + ".074Z");
        
        JsonObject staff = visit.get("staff").getAsJsonObject();
        staff.remove("name");
        staff.addProperty("name", txtDoctor.getText());
        
        JsonObject patient = visit.get("patient").getAsJsonObject();
        String[] arr = txtPatientName.getText().split(" ");
        patient.remove("firstName");
        patient.addProperty("firstName", arr[0]);
        patient.remove("firstName");
        patient.addProperty("firstName", arr[1]);
        patient.remove("id");
        patient.addProperty("id", Integer.valueOf(txtPatientId.getText()));
        visit.remove("note");
        visit.addProperty("note", txtNote.getText());
        
        System.out.println(visit.toString());
        //LesionDTO lesionDTO = new LesionDTO(Integer.parseInt(txtLesionId.getText()), txtLocation.getText(), txtDateLession.getText(), txtTimeLession.getText());
        RequestHandler.updateLession(visit.toString(), idVisit);   
    }
   
}
