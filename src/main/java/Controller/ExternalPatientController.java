package Controller;

import Model.ExternalPatient;
import Model.Patient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ExternalPatientController {
    @FXML
    Button searchBtn;
    @FXML
    TextField searchField;
    @FXML
    ComboBox byCB;
    @FXML
    ComboBox pageCB;
    @FXML
    Label internal;
    @FXML
    Label other;
    @FXML
    TableView table;
    @FXML
    TableColumn idCol;
    @FXML
    TableColumn nameCol;
    @FXML
    TableColumn sexCol;
    @FXML
    TableColumn dobCol;

    public static String[] hospital;
    public List<ExternalPatient> patients;

    public ExternalPatientController() {
    }


    @FXML
    public void initialize() {

        patients = getPatientsByHospital(hospital[0]);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        sexCol.setCellValueFactory(new PropertyValueFactory<>("sex"));
        dobCol.setCellValueFactory(new PropertyValueFactory<>("dob"));

        table.getItems().addAll(patients);
        byCB.getItems().addAll("Name", "Date of Birth");
        byCB.getSelectionModel().selectFirst();

        searchBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                List<ExternalPatient> foundList = new LinkedList<>();
                for (ExternalPatient patient: patients) {
                    if (String.valueOf(byCB.getValue()).equals("Name")) {
                        if (patient.getName().contains(searchField.getText())) {
                            foundList.add(patient);
                        }
                    } else {
                        if (patient.getDob().contains(searchField.getText())) {
                            foundList.add(patient);
                        }
                    }
                }
                table.getItems().clear();
                table.getItems().addAll(foundList);
            }
        });

        ScreenController screenController = new ScreenController();
        // back to internal
        internal.setCursor(Cursor.HAND);
        internal.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                screenController.closeScreen((Stage) internal.getScene().getWindow());
                screenController.openScreen("patientRecord");

            }
        });

        // go to lesion search
        other.setCursor(Cursor.HAND);
        other.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                screenController.closeScreen((Stage) other.getScene().getWindow());
                screenController.openScreen("map");

            }
        });
    }


    public List<ExternalPatient> getPatientsByHospital(String id) {
        OkHttpClient client = new OkHttpClient();
        String message = "";
        String url = "http://visiderm.herokuapp.com/api/v1/clinic/"+id+"/patients";
        if (id == "") url = "http://visiderm.herokuapp.com/api/v1/patients";
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("User-Agent", "PostmanRuntime/7.20.1")
                .addHeader("Accept", "*/*")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "0f0df8a4-6f13-4ae0-9168-39a78469c3d5,e4d41edb-130b-43ce-bbe6-c219da9640b8")
                .addHeader("Host", "visiderm.herokuapp.com")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                message =  "Unexpected code " + response;
            } else {
                // Get response body
                message =  Objects.requireNonNull(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonArray patients = new JsonParser().parse(message).getAsJsonObject().get("content").getAsJsonArray();
        List<ExternalPatient> patientList = new LinkedList<>();
        for (JsonElement patient: patients) {
            ExternalPatient patientModel = new ExternalPatient();
            patientModel.setId(patient.getAsJsonObject().get("id").getAsString());
            patientModel.setName(patient.getAsJsonObject().get("firstName").getAsString()+" "+
                    patient.getAsJsonObject().get("lastName"));
            patientModel.setSex(patient.getAsJsonObject().get("sex").getAsString());
            patientModel.setDob(patient.getAsJsonObject().get("dob").getAsString());
            patientList.add(patientModel);
        }
        return patientList;
    }

}
