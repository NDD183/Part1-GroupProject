package Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
/**
 * This class created to control the all screen behaviors in the application
 *
 * @author s3634096
 *
 */
public class ScreenController {

    public void closeScreen(Stage stage){
        stage.close();
    }

    public void openScreen(String filename) {
        Stage stage = new Stage();
        Scene scene = null;

        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("/GUI/" + filename+".fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setTitle("VisiDerm System");
        stage.setOnCloseRequest(e -> closeProgram(filename) );
        stage.show();

    }

    public void closeProgram(String filename){
//        if(!filename.equals("home.fxml")) {
//            openScreen("home.fxml", "Home Page");
//        } else {
//            System.out.println("Waiting to serialize before exiting application");
//            System.out.println("Serialize done");
//        }
    }
}

