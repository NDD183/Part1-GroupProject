package Implementation;

import Model.Lesion;
import Model.LesionHistory;
import Model.Visit;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import okhttp3.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class HttpImpl {

    /**Name: initialize
     **Event: When the patientRecord.fxml is loaded
     **Purpose: Setting UI component before showing to user
     **Passed: All UI components named correctly
     **Returns: void
     **Input: void // Output: The record  UI -  the patientRecord.fxml is loaded successfully
     **Effect: The method help user to additionally config UI
     */
    public  String[] exactHttpResponse(String type, JsonObject patient) {
        String[] infos = {};
        if(type.equals("patient")) {
            infos = new String[]{"id", "firstName", "lastName","title", "sex", "maritalStatus", "dob", "address", "suburb", "postcode",
                    "country", "homePhone", "officePhone", "mobilePhone", "fax", "email", "occupation", "nextToKin", "kinPhoneNumber"};
        }
        if(type.equals("visit")) {
            infos = new String[]{"id", "startDate", "endDate", "reason", "note"};

        }
        if(type.equals("clinic")) {
            infos = new String[]{"id", "name", "city", "country"};
        }
        if(type.equals("lesion")) {
            infos = new String[]{"id", "location", "status"};
        }
        if(type.equals("lesionHistory")) {
            infos = new String[]{"id", "date", "note", "size","type", "diagnoses","color"};
        }
        for (int i = 0; i < infos.length; i++) {
            String info = String.valueOf(patient.get(infos[i]));
            if(i != 0) {
                info = info.substring(1, info.length() - 1);
            }
            infos[i] = info;
        }
        return infos;
    }

    /**Name: fetchGetRequest
     ** Purpose: This method helps to load all patient record info to UI
     */
    public String fetchGetRequest(String url) {

        String message = "";

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
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
        return  message;
    }

    public Boolean fetchPutRequest(String url, String requestBody) {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestBody);
        Request request = new Request.Builder()
                .url(url)
                .method("PUT", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                return Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  Boolean.FALSE;
    }




}
