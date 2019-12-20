package Implementation;

import Model.Clinic;
import Model.Lesion;
import Model.LesionHistory;
import Model.Visit;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;


public class LesionImpl {

    /** Name: addPatient
     * Purpose: This method helps to add course.
     * @param lesion - Contains all the details of staff such as id or name ,...
     * @param lesionMap - Contains staff information in the staff map
     * @return String - Contains "OK" or "Error"
     */
    public Map<Long, Lesion> addLesion(Lesion lesion, Map<Long, Lesion> lesionMap) {
        if(lesionMap != null) {
            if(!lesionMap.containsKey(lesion.getId())) {
                System.out.println("Updating Lesion Info");
            }
            //Add lesion to map
            lesionMap.put(lesion.getId(), lesion);
        }
        return lesionMap;
    }

    public  ArrayList<Lesion> loadLesionData(String response) {
        HttpImpl httpImpl = new HttpImpl();
        ArrayList<Lesion> lesionsList = new ArrayList<>();
        Set<LesionHistory> lesionHistories = new HashSet<>();
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        JsonArray contentResponse = (JsonArray) jsonObject.get("content");

        for (int i = 0; i < contentResponse.size(); i++) {
            String[] infos = httpImpl.exactHttpResponse("lesion", contentResponse.get(i).getAsJsonObject());
            Gson gson = new Gson();
            Visit visit = gson.fromJson(contentResponse.get(i).getAsJsonObject().get("visit"), Visit.class);
            JsonArray lesionHisArray = contentResponse.get(i).getAsJsonObject().get("lesionHistory").getAsJsonArray();

            long lid;
            try {
                lid = Long.parseLong(infos[0]);
            } catch (NumberFormatException nfe) {
                System.out.println(nfe.toString());
                break;
            }

            if (lesionHisArray.size() > 0) {
                int index = 0;

                while (index < lesionHisArray.size()) {
                    // LesionHistory lesionHistory = gson.fromJson(lesionHisArray.get(index), LesionHistory.class);
                    String[] lesHisInfos = httpImpl.exactHttpResponse("lesionHistory", lesionHisArray.get(index).getAsJsonObject());
                    String[] dates = lesHisInfos[1].split("T");

                    int hid = Integer.parseInt(String.valueOf(lesionHisArray.get(index).getAsJsonObject().get("id")));
                    int imageName = 24306 + hid - 1;
                    String pathImage = "";
                    if (imageName > 24594) {
                        imageName = ThreadLocalRandom.current().nextInt(24306, 24594 + 1);
                    }
                    pathImage = "/Lesions/ISIC_00" + imageName + ".jpg";
                    System.out.println(pathImage);

                    BufferedImage buffImage = null;
                    ImageView lesionImage = null;

                    try {
                        buffImage = ImageIO.read(getClass().getResourceAsStream(pathImage));
                        lesionImage = new ImageView((SwingFXUtils.toFXImage(buffImage, null)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    LesionHistory lesionHistory = new LesionHistory(Long.parseLong(lesHisInfos[0]), dates[0],
                            lesHisInfos[2], lesHisInfos[3], lesHisInfos[4], lesHisInfos[5], lesHisInfos[6],
                            dates[1].substring(0, 5), lesionImage);
                   // lesionHistory.setDate(dates[0]);
                    //lesionHistory.setTime(dates[1].substring(0, 5));
                    lesionHistories.add(lesionHistory);
                    index++;
                }
            }
            lesionsList.add(new Lesion(lid, visit, infos[1],  infos[2], lesionHistories));
        }
        return lesionsList;
    }

}
