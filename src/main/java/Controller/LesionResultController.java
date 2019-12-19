package Controller;
/**
 * This class created to handle the logic when user interact with lesion result UI (screen 19)
 *
 * @author s3634096
 *
 */
import Implementation.HttpImpl;
import Model.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
     TableColumn<Lesion, Integer> hosColumn;
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
    private Gson gson = new Gson();

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
                String response = httpImpl.fetchGetRequest("http://visiderm.herokuapp.com/api/v1/lesions/1");
                JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                JsonArray lesionHistories = (JsonArray) jsonObject.get("lesionHistory");

                String[] infos = httpImpl.exactHttpResponse("lesionHistory",  lesionHistories.get(0).getAsJsonObject());
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
                    break;
            case "Colour":
                    break;
            default:
                    System.out.println("Invalid search type");
        break;
        }
    }


    public void getSearchInfo(String kw, String type) {
        keyword = kw;
        searchType = type;
    }
}
