package Driven;

import Controller.RecordController;
import Controller.ScreenController;
import javafx.application.Application;
import javafx.stage.Stage;

public class StartApplication extends Application {

    private ScreenController screenController = new ScreenController();
    private RecordController recordController = new RecordController();


    /**Name: start
     **Purpose: execute UI when the class Main is run by user or in other word the application is started
     **Passed: The UI is config correctly
     **Returns: void
     **Input: Stage // Output: Void
     **Effect: This methods will help to open UI when the application run
     */
    @Override
    public void start(Stage stage) throws Exception {
        recordController.getLoadCode(101);
        screenController.openScreen("login");
    }

    public static void main(String[] args) {
        launch(args);
    }

}
