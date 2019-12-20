/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package component;

import com.google.gson.Gson;
//import model.LesionDTO;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class RequestHandler {
    public static String getVisitById(String id) {
        return getUrlContents("http://visiderm.herokuapp.com/api/v1/visits/"+id);
    }

    public static String getVisitsByPatient(int id) {
        return getUrlContents("http://visiderm.herokuapp.com/api/v1/patients/" + id + "/visits");
    }

    public static String getLessionJson(String idVisit) {
        return getUrlContents("http://visiderm.herokuapp.com/api/v1/visits/" + idVisit + "/lesions");
    }

    public static String getLessionJsonById(String idLession) {
        return getUrlContents("http://visiderm.herokuapp.com/api/v1/lesions/" + idLession);
    }

    public static String getUpdateLession(int idLession) {
        return getUrlContents("http://visiderm.herokuapp.com/api/v1/lesions/" + idLession);
    }

    private static String getUrlContents(String theUrl) {
        StringBuilder content = new StringBuilder();

        // many of these calls can throw exceptions, so i've just
        // wrapped them all in one try/catch statement.
        try {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

//    public static void updateLession(LesionDTO lesion, String visitId) {
//        OkHttpClient client = new OkHttpClient();
//
//        MediaType mediaType = MediaType.parse("application/json");
//        String jsonBody = new Gson().toJson(lesion);
//        System.out.println(jsonBody);
//        RequestBody body = RequestBody.create(mediaType, jsonBody);
//        Request request = new Request.Builder()
//                .url("http://visiderm.herokuapp.com/api/v1/visits/" + visitId + "/lesions")
//                .put(body)
//                .addHeader("Content-Type", "application/json")
//                .addHeader("User-Agent", "PostmanRuntime/7.20.1")
//                .addHeader("Accept", "*/*")
//                .addHeader("Cache-Control", "no-cache")
//                .addHeader("Postman-Token",
//                           "89ebcb02-1691-443d-b3fd-7cf4371ace94,3995d768-1588-4e90-933c-dfd4edead536")
//                .addHeader("Host", "visiderm.herokuapp.com")
//                .addHeader("Accept-Encoding", "gzip, deflate")
//                .addHeader("Content-Length", "455")
//                .addHeader("Connection", "keep-alive")
//                .addHeader("cache-control", "no-cache")
//                .build();
//
//        try {
//            Response response = client.newCall(request).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void updateLession(String jsonBody, String visitId) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("http://visiderm.herokuapp.com/api/v1/visits/" + visitId)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "PostmanRuntime/7.20.1")
                .addHeader("Accept", "*/*")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token",
                           "89ebcb02-1691-443d-b3fd-7cf4371ace94,3995d768-1588-4e90-933c-dfd4edead536")
                .addHeader("Host", "visiderm.herokuapp.com")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Content-Length", "455")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
