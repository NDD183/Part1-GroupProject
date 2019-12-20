package Controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import component.ParseJson;
import component.RequestHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LesionRecordController implements Initializable {

    @FXML
    private Label lbLessionId;
    @FXML
    private Label lbDoctor;
    @FXML
    private Label lbPatient;
    @FXML
    private Label lbLessionLocation;
    @FXML
    private Label lbStatus;
    
    @FXML
    private Label lbDateLesion;
    @FXML
    private Label lbTimeLesion;
    @FXML
    private Label lbSizeLesion;
    @FXML
    private TextField txtNoteLesion;
    @FXML
    private Label accLab;
    @FXML
    private Label searchLab;

    public void accessClicked(MouseEvent mouseEvent) {
    }

    public void searchClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadLessionByVisit("1");
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

    private void loadLessionByVisit(String id) {
        String json = RequestHandler.getLessionJsonById(id);
        JsonObject object = new JsonParser().parse(json).getAsJsonObject();
        JsonObject visit = ParseJson.getByKey(object, "visit");
        JsonObject staff = ParseJson.getByKey(visit, "staff");
        JsonObject patient = ParseJson.getByKey(visit, "patient");

        lbLessionId.setText(object.get("id").getAsString());
        lbDoctor.setText(staff.get("name").getAsString());
        lbPatient.setText(
                patient.get("firstName").getAsString() + patient.get("lastName").getAsString());
        lbLessionLocation.setText(object.get("location").getAsString());
        lbStatus.setText(object.get("status").getAsString());
        
        JsonObject lesionHistory = ParseJson.getByKey(object, "lesionHistory");
        lbDateLesion.setText(lesionHistory.get("date").getAsString().substring(0, 10));
        lbTimeLesion.setText(lesionHistory.get("date").getAsString().substring(12, 19));
        lbSizeLesion.setText(lesionHistory.get("size").getAsString());
        txtNoteLesion.setText(lesionHistory.get("note").getAsString());
    }
}
