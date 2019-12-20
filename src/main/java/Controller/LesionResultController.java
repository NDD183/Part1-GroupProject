package Controller;
/**
 * This class created to handle the logic when user interact with lesion result UI (screen 19)
 *
 * @author s3634096
 *
 */
import Implementation.HttpImpl;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LesionResultController implements Initializable {

    // Initialize UI component
    @FXML
    Label hosNameLab;
    @FXML
    Label backLab;
    @FXML
    Label accLab;
    @FXML
    Label dateLab;
    @FXML
    Label timeLab;
    @FXML
    Label sizeLab;
    @FXML
    Label lidLab;
    @FXML
    TextArea diagArea;
    @FXML
    Label foundLab;
    @FXML
    TableView<Lesion> lesionTable;
    @FXML
    ImageView lesView;
    @FXML
     TableColumn<Lesion, String> lidColumn;
    @FXML
     TableColumn<Lesion, String> docColumn;
    @FXML
     TableColumn<Lesion, String> dateColumn;
    @FXML
     TableColumn<Lesion, String> timeColumn;
    @FXML
     TableColumn<Lesion, String> hosColumn;
    @FXML
     TableColumn<Lesion, String> diagColumn;
    @FXML
     TableColumn<Lesion, ImageView> imgColumn;
    @FXML
    Pagination lesionPage;

    private ScreenController screenController = new ScreenController();
    private HttpImpl httpImpl = new HttpImpl();
    private static String keyword;
    private static String searchType;
    private final static int lrowsPerPage = 10;
    private Gson gson = new Gson();
    private LesionImpl lesionImpl = new LesionImpl();
    private String response;
    private JsonObject jsonObject;
    private JsonArray contentResponse;
    private String[] infos;
    private ObservableList<Lesion> lesionData;
    ArrayList<Lesion> lesionsList = new ArrayList<>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set hover when moving mouse to specific label
        accLab.setCursor(Cursor.HAND);
        backLab.setCursor(Cursor.HAND);

        // Fetch data base on keyword and search type
        loadData();

    }

    public void backClicked(MouseEvent mouseEvent) {
        System.out.println("back clicked");
    }

    public void accClicked(MouseEvent mouseEvent) {
        System.out.println("access  clicked");
    }

    public void logClicked(MouseEvent mouseEvent) {
        screenController.closeScreen((Stage) hosNameLab.getScene().getWindow());
        screenController.openScreen("login");
    }

    public void loadData() {


        switch (searchType) {
            case "Lesion ID":
                response = httpImpl.fetchGetRequest("http://visiderm.herokuapp.com/api/v1/lesions/" + keyword);
                jsonObject = new JsonParser().parse(response).getAsJsonObject();
                JsonArray lesionHistories = (JsonArray) jsonObject.get("lesionHistory");

                infos = httpImpl.exactHttpResponse("lesionHistory",  lesionHistories.get(0).getAsJsonObject());
                String[] dates = infos[1].split("T");

                dateLab.setText("Date: " + dates[0]);
                timeLab.setText("Time: " + dates[1].substring(0,5));
                sizeLab.setText("Size: " + infos[3] +" cm");
                lidLab.setText("Lesion ID: " + jsonObject.get("id"));
                diagArea.setText(infos[5]);

                int imgID = 24306 + Integer.parseInt(infos[0]) -1;
                String imageName = "/Lesions/ISIC_00"+  imgID   + ".jpg";

                try {
                    BufferedImage bufferedImage = ImageIO.read(getClass().getResourceAsStream(imageName));
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    lesView.setImage(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case "Diagnoses":
                 break;
            case "Lesion Type":


                 break;
            case "Size":
                response = httpImpl.fetchGetRequest("http://visiderm.herokuapp.com/api/v1/lesions?size="+ keyword);
                lesionsList = lesionImpl.loadLesionData(response);

                // Configure table cell height
                lesionTable.setFixedCellSize(60.0);

                // Check if list is empty
                if (lesionsList != null) {

                    // Set text alignment for each column in record table
                    lidColumn.setStyle("-fx-alignment: CENTER;");
                    docColumn.setStyle("-fx-alignment: CENTER;");
                    dateColumn.setStyle("-fx-alignment: CENTER;");
                    timeColumn.setStyle("-fx-alignment: CENTER;");
                    hosColumn.setStyle("-fx-alignment: CENTER;");
                    diagColumn.setStyle("-fx-alignment: CENTER;");
                    imgColumn.setStyle("-fx-alignment: CENTER;");

                    // Set column in array to present as different attributes of staff
                    lidColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

                    // Insert doctor name and clinic name to the table column
                    docColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Lesion, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<Lesion, String> param) {
                            return new SimpleStringProperty(param.getValue().getVisit().getStaff().getName());
                        }
                    });
                    hosColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Lesion, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<Lesion, String> param) {
                            return new SimpleStringProperty(param.getValue().getVisit().getClinic().getName());
                        }
                    });
                    dateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Lesion, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<Lesion, String> param) {
                            ArrayList<LesionHistory> lesHisList  = new ArrayList<>();
                            lesHisList.addAll(param.getValue().getLesionHistory());

                            for(int i = 0; i< lesHisList.size(); i++) {
                                if(lesHisList.get(i).getSize().contains(keyword)) {
                                    return new SimpleStringProperty(lesHisList.get(i).getDate());
                                }
                            }
                            return new SimpleStringProperty("");
                        }
                    });

                    timeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Lesion, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<Lesion, String> param) {
                            ArrayList<LesionHistory> lesHisList  = new ArrayList<>();
                            lesHisList.addAll(param.getValue().getLesionHistory());

                            for(int i = 0; i< lesHisList.size(); i++) {
                                if(lesHisList.get(i).getSize().contains(keyword)) {
                                    return new SimpleStringProperty(lesHisList.get(i).getTime());
                                }
                            }
                            return new SimpleStringProperty("");
                        }
                    });

                    diagColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Lesion, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<Lesion, String> param) {
                            ArrayList<LesionHistory> lesHisList  = new ArrayList<>();
                            lesHisList.addAll(param.getValue().getLesionHistory());

                            for(int i = 0; i< lesHisList.size(); i++) {
                                if(lesHisList.get(i).getSize().equals(keyword)) {
                                    return new SimpleStringProperty(lesHisList.get(i).getDiagnoses());
                                }
                            }
                            return new SimpleStringProperty("");
                        }
                    });

                    lesionData = FXCollections.observableArrayList(lesionsList);
                    // Set the created list to the staff table
                    lesionTable.setItems(null);
                    lesionTable.setItems(lesionData);

                   //  Applying pagination to record table
                    lesionPage.setCurrentPageIndex(0);
                    lesionPage.setPageCount(lesionData.size() / lrowsPerPage + 1);
                    lesionPage.setPageFactory(this::createLesionPage);
                }
                    break;
            case "Colour":
                    break;
            default:
                    response = "Invalid search type";
        break;
        }
    }


    public void getSearchInfo(String kw, String type) {
        keyword = kw;
        searchType = type;
    }

    /**Name: displayAlert
     ** Purpose: This method helps to display alert in this screen
     * @param pageIndex: contains the information of the context section in alert dialog
     ** @return void
     */
    private Node createLesionPage(int pageIndex) {
        int fromIndex = pageIndex * lrowsPerPage;
        int toIndex = Math.min(fromIndex + lrowsPerPage, lesionsList.size());
        lesionTable.setItems(FXCollections.observableArrayList(lesionsList.subList(fromIndex, toIndex)));
        return lesionTable;
    }
}
