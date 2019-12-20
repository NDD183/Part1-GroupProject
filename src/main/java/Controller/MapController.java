package Controller;

import Model.ExternalPatient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class MapController {
    @FXML
    private Canvas worldMap;
    @FXML
    private Canvas regionMap;
    @FXML
    private ComboBox regionCB;
    @FXML
    private ComboBox cityCB;
    @FXML
    private ComboBox hospitalCB;
    @FXML
    private Label internal;
    @FXML
    private Label lesion;
    @FXML
    private Button regionSearch;
    @FXML
    private Button hospitalSearch;

    public String[][] hospitalList;
    public static String[] hospital;

    public MapController() {
    }

    @FXML
    public void initialize() throws URISyntaxException, FileNotFoundException {
        // load map data
        hospitalList = getHospitals();

        // world map
        Image wmImage = new Image("/maps/world-map.gif");
        GraphicsContext wmgc = worldMap.getGraphicsContext2D();
        wmgc.drawImage(wmImage, 0, 0, worldMap.getWidth(), worldMap.getHeight());
        int[][] coors = getCoordinates();
        for (int i = 0; i < 6; i++) {
            wmgc.strokeText("X", coors[i][0], coors[i][1]);
        }
        worldMap.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int[] clickedCoors = {(int) event.getX(), (int) event.getY()};
                for (int i = 0; i < 6; i++) {
                    if (clicked(clickedCoors, coors[i])) {
                        String region = getRegionByCoordinates(coors[i]);
                        drawMap(region);
                        regionCB.getSelectionModel().select(region);
                    }
                }
            }
        });

        // region map
        for (String region : getRegions()) {
            regionCB.getItems().add(region);
        }
        regionSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String region = regionCB.getValue().toString();
                drawMap(region);
            }
        });

        // search hospital open external patient list
        ScreenController screenController = new ScreenController();
        hospitalSearch.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                hospital = getHospitalByName(String.valueOf(hospitalCB.getValue()));
                ExternalPatientController.hospital = hospital;
                screenController.closeScreen((Stage) hospitalSearch.getScene().getWindow());
                screenController.openScreen("externalPatient");
            }
        });

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
        lesion.setCursor(Cursor.HAND);
        lesion.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                screenController.closeScreen((Stage) lesion.getScene().getWindow());
                screenController.openScreen("lesionsSearch");

            }
        });

    }

    public void drawMap(String region) {
        Image regionImage = new Image("/maps/"+region+".jpg");
        GraphicsContext regiongc = regionMap.getGraphicsContext2D();
        regiongc.drawImage(regionImage, 0, 0, regionMap.getWidth(), regionMap.getHeight());
        String[] cities = getCitiesByRegion(region);
        cityCB.getItems().clear();
        for (String city: cities) {
            cityCB.getItems().add(city);
        }
        cityCB.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                hospitalCB.getItems().clear();
                String[] hospitals = getHospitalsByCity(String.valueOf(cityCB.getValue()));
                for (String hospital: hospitals) {
                    hospitalCB.getItems().add(hospital);
                }
            }
        });

    }

    public String[] getHospitalByName(String name) {
        for (String[] hospital : hospitalList) {
            if (hospital[1].equals(name)) {
                return hospital;
            }
        }
        return new String[] {""};
    }

    // check if a region defined by coors[] is clicked
    public boolean clicked(int[] clicked, int[] coors) {
        int diff = 50; // detection box of 100x100 with X mark as center
        return (clicked[0] > coors[0]-diff && clicked[0] < coors[0]+diff
                && clicked[1] > coors[1]-diff && clicked[1] < coors[1]+diff);
    }

    public String[] getRegions() {
        return new String[] {"North America", "South America", "Africa", "Oceania", "Europe", "Asia"};
    }

    public int[][] getCoordinates() {
        return new int[][] {{500, 200}, {130, 230}, {200, 400}, {350, 300}, {550, 400}, {350, 200}};
    }

    public String getRegionByCoordinates(int[] coors) {
        if (coors[0] == 500 && coors[1] == 200) return "Asia";
        if (coors[0] == 130 && coors[1] == 230) return "North America";
        if (coors[0] == 200 && coors[1] == 400) return "South America";
        if (coors[0] == 350 && coors[1] == 300) return "Africa";
        if (coors[0] == 550 && coors[1] == 400) return "Oceania";
        if (coors[0] == 350 && coors[1] == 200) return "Europe";
        return "";
    }

    public int[] getCoordinatesByRegion(String region) {
        switch (region) {
            case "Asia":
                return new int[]{500, 200};
            case "North America":
                return new int[]{130, 230};
            case "South America":
                return new int[]{200, 400};
            case "Africa":
                return new int[]{350, 300};
            case "Oceania":
                return new int[]{550, 400};
            case "Europe":
                return new int[]{350, 200};
            default:
                return new int[]{};
        }
    }

    public String[] getCitiesByRegion(String region) {
        switch (region) {
            case "Asia":
                return new String[]{"Kingstown", "Nuuk", "North Nicosia"};
            case "North America":
                return new String[]{};
            case "South America":
                return new String[]{"Muscat", "Niamey"};
            case "Africa":
                return new String[]{"Pyongyang", "Berlin", "Nassau"};
            case "Oceania":
                return new String[]{};
            case "Europe":
                return new String[]{"Brasilia", "Apia"};
            default:
                return new String[]{};
        }
    }

    public String[] getHospitalsByCity(String city) {

        List<String> retval = new LinkedList<>();
        for (String[] hospital : hospitalList) {

            if (hospital[2].equals(city)) {
                retval.add(hospital[1]);
            }
        }
        String[] retvalStr = new String[retval.size()];
        retvalStr = retval.toArray(retvalStr);
        return retvalStr;
    }



    public String[][] getHospitals() {
        OkHttpClient client = new OkHttpClient();
        String message = "";
        String url = "http://visiderm.herokuapp.com/api/v1/clinics";
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
        JsonArray hospitals = new JsonParser().parse(message).getAsJsonObject().get("content").getAsJsonArray();
        String hospitalsStr[][] = new String[hospitals.size()][4];
        int i = 0;
        for (JsonElement hospital: hospitals) {
            hospitalsStr[i][0] = hospital.getAsJsonObject().get("id").getAsString();
            hospitalsStr[i][1] = hospital.getAsJsonObject().get("name").getAsString();
            hospitalsStr[i][2] = hospital.getAsJsonObject().get("city").getAsString();
            hospitalsStr[i][3] = hospital.getAsJsonObject().get("country").getAsString();
            i++;
        }
        return  hospitalsStr;
    }

}
