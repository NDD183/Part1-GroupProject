package Controller;

import Model.Visit;
import Model.Visit2;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import component.ParseJson;
import component.RequestHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class VisitController implements Initializable {

    @FXML
    private Label txtDate;
    @FXML
    private Label txtTime;
    @FXML
    private Label txtDoctor;
    @FXML
    private Label txtClinic;
    @FXML
    private Label txtPatientName;
    @FXML
    private Label txtPatientId;
    @FXML
    private Label lbNote;
    @FXML
    private ImageView imageV;
    @FXML
    private Pane pane;
    @FXML
    private Label accLab;
    @FXML
    private Label searchLab;

    private int defaultId = 1;
    public static int selectedVisitID;
    public static int selectedPatientID;
    public static int nextVisitID;
    public static int i;
    List<Visit2> visitList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadVisitRecord(selectedVisitID);
        String json = RequestHandler.getVisitsByPatient(selectedPatientID);
        JsonArray visits = new JsonParser().parse(json).getAsJsonObject().get("content").getAsJsonArray();
        visitList= new LinkedList<>();
        for (JsonElement visit : visits) {
            Visit2 myVisit = new Visit2();
            myVisit.setStartDate(visit.getAsJsonObject().get("startDate").getAsString());
            myVisit.setEndDate(visit.getAsJsonObject().get("endDate").getAsString());
            myVisit.setClinicName(visit.getAsJsonObject().get("clinic").getAsJsonObject().get("name").getAsString());
            myVisit.setPatientFirstName(visit.getAsJsonObject().get("patient").getAsJsonObject().get("firstName").getAsString());
            myVisit.setPatientId(visit.getAsJsonObject().get("patient").getAsJsonObject().get("id").getAsString());
            myVisit.setStaffName(visit.getAsJsonObject().get("staff").getAsJsonObject().get("name").getAsString());
            myVisit.setNote(visit.getAsJsonObject().get("note").getAsString());
            visitList.add(myVisit);
        }
        i = 0;

//        txtDate.setText(visit.getStartDate());
//        txtTime.setText(visit.getDuration());
//        txtClinic.setText(visit.getStaff().getName());

//        pane.setOnMouseClicked(e -> {
//            System.out.println("["+e.getX()+", "+e.getY()+"]");
//        });
      //  drawImage("");
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

    private void loadVisitRecord(int id) {
        String json = RequestHandler.getVisitById(String.valueOf(selectedVisitID));
        JsonObject visit = ParseJson.getJson(json);
        txtDate.setText(visit.get("startDate").getAsString().substring(0, 10));
        txtTime.setText(visit.get("endDate").getAsString().substring(12, 19));
        txtClinic.setText(visit.get("clinic").getAsJsonObject().get("name").getAsString());

        JsonObject patient = ParseJson.getByKey(visit, "patient");
        txtPatientName.setText(patient.get("firstName").getAsString());
        txtPatientId.setText(patient.get("id").getAsString());

        JsonObject staff = ParseJson.getByKey(visit, "staff");
        txtDoctor.setText(staff.get("name").getAsString());
        lbNote.setText(visit.get("note").getAsString());
    }

    @FXML
    public void updateRecord(ActionEvent e) {
        ScreenController control = new ScreenController();
        control.closeScreen((Stage) txtDate.getScene().getWindow());
        VisitRecordController.selectedVisitID = selectedVisitID;
        control.openScreen("visitRecord");
    }
    
    @FXML
    public void makeNewRecord() {
        ScreenController control = new ScreenController();
        control.closeScreen((Stage) txtDate.getScene().getWindow());
        control.openScreen("lessionRecord");
    }
    
    @FXML
    public void nextRecord() {
        i++;
        Visit2 visit = visitList.get(i);
        txtDate.setText(visit.getStartDate());
        txtTime.setText(visit.getEndDate());
        txtClinic.setText(visit.getClinicName());

        txtPatientName.setText(visit.getPatientFirstName());
        txtPatientId.setText(visit.getPatientId());

        txtDoctor.setText(visit.getStaffName());
        lbNote.setText(visit.getNote());
    }

    void drawImage(String src) {
        final Image image = new Image(getClass().getResourceAsStream("/download.png"));

        final Canvas canvas = new Canvas(pane.getPrefWidth(), pane.getPrefHeight());
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setGlobalBlendMode(BlendMode.SCREEN);

        gc.drawImage(image, 0, 0);
        // create a Group 
        Group group = new Group(canvas);
        pane.getChildren().add(group);
    }

}
