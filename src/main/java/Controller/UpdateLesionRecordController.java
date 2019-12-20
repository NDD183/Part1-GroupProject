package Controller;

import com.google.gson.JsonObject;
import component.ParseJson;
import component.RequestHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateLesionRecordController implements Initializable {
    @FXML
    private TextField txtLesionID;
    @FXML
    private TextField txtDoctor;
    @FXML
    private TextField txtPatient;
    @FXML
    private TextField txtLesionLocation;
    @FXML
    private TextField txtPatientName;
    @FXML
    private TextField txtLesionDate;
    @FXML
    private TextField txtLesionTime;
    @FXML
    private TextField txtLesionSize;
    @FXML
    private TextField txtLesionImage;

    private int defaultId = 1;
    String idLesion;
    JsonObject lesion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateLesionGeneral();
    }

    private void updateLesionGeneral() {
        String json = RequestHandler.getUpdateLession(defaultId);
        lesion = ParseJson.getJson(json);
        System.out.println(lesion);
        txtLesionID.setText(lesion.get("id").getAsString().substring(0, 10));
        txtLesionLocation.setText(lesion.get("location").getAsString().substring(12, 19));

        JsonObject patient = ParseJson.getByKey(lesion, "patient");
        txtPatientName.setText(patient.get("firstName").getAsString() + " " + patient.get("lastName").getAsString());
        txtPatient.setText(patient.get("id").getAsString());

        JsonObject staff = ParseJson.getByKey(lesion, "staff");
        idLesion = lesion.get("id").getAsString();
        //loadLessionByVisit(visit.get("id").getAsString());


    }

    private void updateLesionDetail() {
        String json = RequestHandler.getUpdateLession(defaultId);
        lesion = ParseJson.getJson(json);
        System.out.println(lesion);
        txtLesionDate.setText(lesion.get("startDate").getAsString().substring(0, 10));
        txtLesionLocation.setText(lesion.get("location").getAsString().substring(12, 19));

        JsonObject patient = ParseJson.getByKey(lesion, "patient");
        txtPatientName.setText(patient.get("firstName").getAsString() + " " + patient.get("lastName").getAsString());
        txtPatient.setText(patient.get("id").getAsString());

        JsonObject staff = ParseJson.getByKey(lesion, "staff");
        idLesion = lesion.get("id").getAsString();
        //loadLessionByVisit(visit.get("id").getAsString());


    }

    @FXML
    public void updateRecord(ActionEvent e) {
        JsonObject patientName = lesion.get("patient name").getAsJsonObject();
        patientName.remove("name");
        patientName.addProperty("name", txtPatientName.getText());

        lesion.remove("startDate");
        lesion.addProperty("startDate", txtLesionDate.getText() + "T" + txtLesionTime.getText() + ".074Z");

        JsonObject staff = lesion.get("staff").getAsJsonObject();
        staff.remove("name");
        staff.addProperty("name", txtDoctor.getText());

        JsonObject patient = lesion.get("patient").getAsJsonObject();
        String[] arr = txtPatientName.getText().split(" ");
        patient.remove("firstName");
        patient.addProperty("firstName", arr[0]);
        patient.remove("firstName");
        patient.addProperty("firstName", arr[1]);
        patient.remove("id");
        patient.addProperty("id", Integer.valueOf(txtLesionSize.getText()));
        lesion.remove("size");
        lesion.addProperty("size", txtLesionSize.getText());

        System.out.println(lesion.toString());
        RequestHandler.updateLession(lesion.toString(), idLesion);
    }

}
