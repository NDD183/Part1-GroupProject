package Controller;
/**
 * This class created to handle the logic when user interact with lesion browsing UI (screen 18)
 *
 * @author s3634096
 *
 */
import Implementation.HttpImpl;
import Implementation.LesionHistoryImpl;
import Implementation.LesionImpl;
import Model.*;
import com.google.gson.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class LesionListController implements Initializable {


    // Initialize UI component
    @FXML
    Label hosNameLab;
    @FXML
    Label backLab;
    @FXML
    Label accLab;
    @FXML
    TableView<LesionHistory> lesionTable;
    @FXML
    private TableColumn<LesionHistory, String> dateColumn;
    @FXML
    private TableColumn<LesionHistory, String> sizeColumn;
    @FXML
    private TableColumn<LesionHistory, ImageView> imageColumn;
    @FXML
    Pagination lesionPage;

    // Initialize required variables
    private ScreenController screenController = new ScreenController();
    private LesionResultController lesionResultController = new LesionResultController();
    private HttpImpl httpImpl = new HttpImpl();
    private LesionImpl lesionImpl = new LesionImpl();
    private ObservableList<Lesion> lesionData;
    private List<Lesion> lesionList ;
    private LesionHistoryImpl lesionHistoryImpl = new LesionHistoryImpl();
    private ObservableList<LesionHistory> lesionHistoryData;
    private List<LesionHistory> lesionHistoryList;
    private final static int lrowsPerPage = 20;
    static Map<Long, Lesion> lesionMap = new HashMap<>();
    static Map<Long, LesionHistory> lesionHistoryMap = new HashMap<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set hover when moving mouse to specific label
        accLab.setCursor(Cursor.HAND);
        backLab.setCursor(Cursor.HAND);

        // Load lesion data
        loadData(httpImpl.fetchGetRequest("http://visiderm.herokuapp.com/api/v1/lesions"));
        // Load lesion history from each lesion record to map collection
        addLesionHistory();
        // Load table base on gained data
        reloadTable();


    }

    public void backClicked(MouseEvent mouseEvent) {
        System.out.println("back clicked");
    }

    public void accClicked(MouseEvent mouseEvent) {
        System.out.println("acc clicked");
    }

    public void logClicked(MouseEvent mouseEvent) {
        screenController.closeScreen((Stage) hosNameLab.getScene().getWindow());
        screenController.openScreen("login");
    }


    /**Name: loadRecord
     ** Purpose: This method helps to load all patient record info to UI
     */
    public void reloadTable() {
        // Configure table cell height
        lesionTable.setFixedCellSize(100.0);

        // Check if list is empty
        if (lesionHistoryList != null) {

            // Add list of staff to the observableArrayList
            lesionHistoryData = FXCollections.observableArrayList(lesionHistoryList);

            // Set text alignment for each column in record table
            dateColumn.setStyle("-fx-alignment: CENTER;");
            sizeColumn.setStyle("-fx-alignment: CENTER;");
            imageColumn.setStyle("-fx-alignment: CENTER;");

            // Set column in array to present as different attributes of staff
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
            // Only insert doctor name and clinic name to the table column
            imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageView"));


            // Set the created list to the staff table
            lesionTable.setItems(null);
            lesionTable.setItems(this.lesionHistoryData);

            // Applying pagination to record table
            lesionPage.setCurrentPageIndex(0);
            lesionPage.setPageCount(lesionHistoryData.size() / lrowsPerPage + 1);
            lesionPage.setPageFactory(this::createLesionPage);
        }
    }


    /**Name: initialize
     **Event: When the patientRecord.fxml is loaded
     **Purpose: Setting UI component before showing to user
     **Passed: All UI components named correctly
     **Returns: void
     **Input: void // Output: The record  UI -  the patientRecord.fxml is loaded successfully
     **Effect: The method help user to additionally config UI
     */

    public void loadData(String response) {
//        Set<LesionHistory> lesionHistories = new HashSet<>();
//        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
//        JsonArray contentResponse = (JsonArray) jsonObject.get("content");
//
//        for (int i = 0; i < contentResponse.size(); i++) {
//            String[] infos = httpImpl.exactHttpResponse("lesion", contentResponse.get(i).getAsJsonObject());
//            Gson gson = new Gson();
//            Visit visit = gson.fromJson(contentResponse.get(i).getAsJsonObject().get("visit"), Visit.class);
//            JsonArray lesionHisArray = contentResponse.get(i).getAsJsonObject().get("lesionHistory").getAsJsonArray();
//
//            long lid;
//            try {
//                lid = Long.parseLong(infos[0]);
//            } catch (NumberFormatException nfe) {
//                System.out.println(nfe.toString());
//                break;
//            }
//
//            if (lesionHisArray.size() > 0) {
//                int index = 0;
//
//                while(index < lesionHisArray.size()) {
//                   // LesionHistory lesionHistory = gson.fromJson(lesionHisArray.get(index), LesionHistory.class);
//                    String[]lesHisInfos = httpImpl.exactHttpResponse("lesionHistory",lesionHisArray.get(index).getAsJsonObject() );
//                    String[] dates = lesHisInfos[1].split("T");
//
//                    int hid = Integer.parseInt(String.valueOf(lesionHisArray.get(index).getAsJsonObject().get("id")));
//                    int imageName =  24306 + hid - 1;
//                    String pathImage = "";
//                    if(imageName > 24594) {
//                        imageName =  ThreadLocalRandom.current().nextInt(24306, 24594 + 1);
//                    }
//                    pathImage = "/Lesions/ISIC_00" + imageName + ".jpg";
//                    System.out.println(pathImage);
//
//                    BufferedImage buffImage = null;
//                    ImageView lesionImage = null;
//
//                    try {
//                        buffImage = ImageIO.read(getClass().getResourceAsStream(pathImage));
//                        lesionImage = new ImageView((SwingFXUtils.toFXImage(buffImage, null)));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    LesionHistory lesionHistory = new LesionHistory(Long.parseLong(lesHisInfos[0]), dates[0],
//                            lesHisInfos[2], lesHisInfos[3], lesHisInfos[4], lesHisInfos[5], lesHisInfos[6],
//                            dates[1].substring(0, 5), lesionImage);
//                    lesionHistory.setDate(dates[0]);
//                    lesionHistory.setTime(dates[1].substring(0, 5));
//                  lesionHistories.add(lesionHistory);
//                  index++;
//                }
//            }
        //    addLesion(lid, visit, infos[1], infos[2], lesionHistories);
     //   }
        ArrayList<Lesion> lesionsList = lesionImpl.loadLesionData(response);
        for(int i = 0; i<lesionsList.size();i++) {
            addLesion(lesionsList.get(i).getId(),lesionsList.get(i).getVisit(),lesionsList.get(i).getLocation(),
                    lesionsList.get(i).getStatus(), lesionsList.get(i).getLesionHistory());
        }
    }


    /**Name: addPatient
     ** Purpose: This method helps to add staff to system from the provided file (execute in first run of application only)
     ** @return String - contains "OK": successfully add staff info
     */
    public String addLesion(long id, Visit visit, String location, String status, Set<LesionHistory> lesionHistories){

        // Create new visit model
        Lesion lesion = new Lesion(id, visit, location, status, lesionHistories);


        if (lesionMap == null) {
            lesionMap = lesionImpl.addLesion(lesion, new HashMap<>());
        } else {
            lesionMap = lesionImpl.addLesion(lesion, lesionMap);
        }
        return "OK";
    }
    public String addLesionHistory(){
        Set<LesionHistory> lesionHistories = new HashSet<>();
        if(lesionMap != null) {
            //Iterate through unit Map
            Iterator<Map.Entry<Long, Lesion>> iterator = lesionMap.entrySet().iterator();
            //Iterate using while loop
            while (iterator.hasNext()) {
                Map.Entry<Long, Lesion> element = iterator.next();
                lesionHistories.addAll(element.getValue().getLesionHistory());
            }
        }
        lesionHistoryList = new ArrayList<>(lesionHistories);
        return "OK";
    }

    /**Name: displayAlert
     ** Purpose: This method helps to display alert in this screen
     * @param pageIndex: contains the information of the context section in alert dialog
     ** @return void
     */
    private Node createLesionPage(int pageIndex) {
        int fromIndex = pageIndex * lrowsPerPage;
        int toIndex = Math.min(fromIndex + lrowsPerPage, lesionHistoryList.size());
        lesionTable.setItems(FXCollections.observableArrayList(lesionHistoryList.subList(fromIndex, toIndex)));
        return lesionTable;
    }

}
