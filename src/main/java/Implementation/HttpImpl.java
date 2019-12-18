package Implementation;

import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

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
        OkHttpClient client = new OkHttpClient();
        String message = "";

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
        return  message;
    }
}
