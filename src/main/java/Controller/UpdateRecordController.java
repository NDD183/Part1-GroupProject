package Controller;

import Implementation.HttpImpl;
import Model.Patient;
import Model.Visit;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import okhttp3.*;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * This class created to handle the logic when user interact with update patient record UI (screen 2)
 *
 * @author s3634096
 *
 */
public class UpdateRecordController implements Initializable {

    // Initialize UI component
    @FXML
    Label hosNameLab;
    @FXML
    Label accLab;
    @FXML
    Label searchLab;
    @FXML
    TextField fnameField;
    @FXML
    TextField lnameField;
    @FXML
    TextField pidField;
    @FXML
    TextField dobField;
    @FXML
    TextField addField;
    @FXML
    TextField subField;
    @FXML
    TextField hphoneField;
    @FXML
    TextField ophoneField;
    @FXML
    TextField mphoneField;
    @FXML
    TextField faxField;
    @FXML
    TextField emailField;
    @FXML
    TextField kinField;
    @FXML
    TextField cnumField;
    @FXML
    TextField codeField;
    @FXML
    ComboBox<String> titleBox;
    @FXML
    ComboBox<String> statusBox;
    @FXML
    ComboBox<String> countryBox;
    @FXML
    ComboBox<String> occBox;
    @FXML
    RadioButton maleBtn;
    @FXML
    RadioButton femaleBtn;
    @FXML
    Label createVisitLab;
    @FXML
    Label editPatientLab;
    @FXML
    Label createPatientLab;
    @FXML
    DatePicker dobPicker;
    @FXML
    Button upBtn;
    @FXML
    TableView<Visit> visitTable;
    @FXML
    TableColumn<Visit, Long> idColumn;
    @FXML
    TableColumn<Visit, String> docColumn;
    @FXML
    TableColumn<Visit, String> cliColumn;
    @FXML
    TableColumn<Visit, String> dateColumn;
    @FXML
    Pagination visitPage;

    private ScreenController screenController = new ScreenController();
    private RecordController recordController = new RecordController();
    private HttpImpl httpImpl = new HttpImpl();
    static Patient selectedPatient;
    static Visit selectedVisit;
    static Map<Long, Visit> selectedVisitMap = new HashMap<>();
    static  List<Visit> visitList  =  new ArrayList<>();
    private ObservableList<Visit> visitData;
    ToggleGroup toggleGroup = new ToggleGroup();

    /**Name: initialize
     **Event: When the editRecord.fxml is loaded
     **Purpose: Setting UI component before showing to user
     **Passed: All UI components named correctly
     **Returns: void
     **Input: void // Output: The edit record  UI -  the editRecord.fxml is loaded successfully
     **Effect: The method help user to additionally config UI
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set hover when moving mouse to specific label
        accLab.setCursor(Cursor.HAND);
        searchLab.setCursor(Cursor.HAND);
        editPatientLab.setCursor(Cursor.HAND);
        createPatientLab.setCursor(Cursor.HAND);
        createVisitLab.setCursor(Cursor.HAND);
        // Set value for simple comboBox
        titleBox.getItems().addAll("Mr","Mrs", "Ms", "Dr");
        titleBox.setValue("Mr");
        statusBox.getItems().addAll("Single","Married");
        statusBox.setValue("Single");
        occBox.getItems().addAll("Retired","None","Others");
        occBox.setValue("None");
        // Set value for country comboBox
        ObservableList<String> cities = FXCollections.observableArrayList();
        String[] locales = Locale.getISOCountries();
        for (String countryList : locales) {
            Locale obj = new Locale("", countryList);
            String[] city = { obj.getDisplayCountry() };
            for (int x = 0; x < city.length; x++) {
                cities.add(obj.getDisplayCountry());
            }
        }
        countryBox.setItems(cities);

        // Change date format of dob picker
        dobPicker.setConverter(new StringConverter<LocalDate>() {
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        dobPicker.setOnAction(event -> {
            if(dobPicker.getValue() != null) {
                dobField.setText(dobPicker.getValue().toString());
            }
        });

        // Set visit table behaviour when user double click on each row
        visitTable.setRowFactory(tv -> {
            TableRow<Visit> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    selectedVisit = row.getItem();
                    System.out.println(selectedVisit.getId());
                }
            });
            return row ;
        });

        // Set group for two radio buttons
        maleBtn.setToggleGroup(toggleGroup);
        femaleBtn.setToggleGroup(toggleGroup);
        // Load patient info to UI
        loadPatientInfo();
        // Load visit list of patient
      //  System.out.println(visitList.get(0).getId());
        loadVisitTable();

        accLab.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                screenController.closeScreen((Stage) accLab.getScene().getWindow());
                screenController.openScreen("map");
            }
        });




    }

    public void loadPatientInfo() {
        String names[] = selectedPatient.getName().split(" ");
        fnameField.setText(names[0]);
        lnameField.setText(names[1]);
        pidField.setText(String.valueOf(selectedPatient.getId()));
        dobField.setText(selectedPatient.getDob());
        addField.setText(selectedPatient.getAddress());
        subField.setText(selectedPatient.getSub());
        codeField.setText(selectedPatient.getPostcode());
        hphoneField.setText(selectedPatient.getHphone());
        ophoneField.setText(selectedPatient.getOphone());
        mphoneField.setText(selectedPatient.getMphone());
        faxField.setText(selectedPatient.getFax());
        emailField.setText(selectedPatient.getEmail());
        kinField.setText(selectedPatient.getNtk());
        cnumField.setText(selectedPatient.getKphone());
        statusBox.setValue(selectedPatient.getMaritalStatus());
        countryBox.setValue(selectedPatient.getCountry());
        occBox.setValue(selectedPatient.getOccupation());
        if(selectedPatient.getSex().equals("Male")) {
            maleBtn.setSelected(true);
            titleBox.setValue("Mr");
        } else {
            femaleBtn.setSelected(true);
            titleBox.setValue("Mrs");
        }
        dobPicker.setPromptText(dobField.getText());
    }

    public void loadVisitTable() {
        if (selectedVisitMap != null) {
            // Transform map to array list
         //  visitList = new ArrayList<>(selectedVisitMap.values());

            // Add list of staff to the observableArrayList
            visitData = FXCollections.observableArrayList(visitList);

            // Set column in array to present as different attributes of staff
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));


            // Only insert doctor name and clinic name to the table column
            docColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStaff().getName()));
            cliColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getClinic().getName()));

            // Set the created list to the staff table
            visitTable.setItems(null);
            visitTable.setItems(this.visitData);

            // Applying pagination to record table
            visitPage.setCurrentPageIndex(0);
            visitPage.setPageCount(visitList.size() / 10 + 1);
            visitPage.setPageFactory(this::createVisitPage);
        }
    }


    public void accessClicked(MouseEvent mouseEvent) {
        System.out.println("access clicked");
    }

    public void searchClicked(MouseEvent mouseEvent) {
        screenController.closeScreen((Stage) searchLab.getScene().getWindow());
        screenController.openScreen("lesionsSearch");
        System.out.println("search clicked");
    }

    public void newVisitClicked(MouseEvent mouseEvent) {
        System.out.println("new visit clicked");

    }

    public void editPatientClicked(MouseEvent mouseEvent) {
        System.out.println("edit patient clicked");

    }

    public void createPatientClicked(MouseEvent mouseEvent) {
        System.out.println("create patient clicked");

    }


    public void updatePressed(ActionEvent actionEvent) {
        if(validation()) {
            if(updatePatientInfo()) {
                recordController.getLoadCode(101);
                screenController.closeScreen((Stage) upBtn.getScene().getWindow());
                screenController.openScreen("patientRecord");
            } else {
                displayErrorAlert("Message from system", "Error Update", "There is an error in server" +
                        ". Please kindly try again !!!");
            }

        }

    }

    public void cancelPressed(ActionEvent actionEvent) {
        recordController.getLoadCode(202);
        screenController.closeScreen((Stage) upBtn.getScene().getWindow());
        screenController.openScreen("patientRecord");
    }

    public void logClicked(MouseEvent mouseEvent) {
        screenController.closeScreen((Stage) searchLab.getScene().getWindow());
        screenController.openScreen("login");
    }





    public Boolean validation() {

        // Check if all required field filled
        if(fnameField.getText().equals("") || lnameField.getText().equals("") || pidField.getText().equals("")
            || dobField.getText().equals("") || addField.getText().equals("") || subField.getText().equals("")
            || codeField.getText().equals("") || hphoneField.getText().equals("") || ophoneField.getText().equals("")
            || mphoneField.getText().equals("") || faxField.getText().equals("")|| emailField.getText().equals("")
            || kinField.getText().equals("") || cnumField.getText().equals("")) {
            displayErrorAlert("Error Updating Record", "Missing Info", "Please fill all"+
                    "fields to update patient record");
            return Boolean.FALSE;
        }
        // Check if some fields contains only number
        if(!isInteger(pidField.getText())  || !isInteger(hphoneField.getText()) ||
           !isInteger(ophoneField.getText())  || !isInteger(mphoneField.getText()) || !isInteger(faxField.getText()) ) {
            displayErrorAlert("Error Updating Record", "Invalid Input", "ID/Post Code/Phone/Number " +
                    "fields only accept numbers");
            return Boolean.FALSE;
        }
        // Check if email field contains a valid email address
        if(!isValidEmail(emailField.getText())) {
            displayErrorAlert("Error Updating Record", "Invalid Input", "Wrong email address ");
            return Boolean.FALSE;
        }
        // Check if dob field contains a valid value
        if(!isValidDOB(dobField.getText())) {
            displayErrorAlert("Error Updating Record", "Invalid Input", "Wrong date of birth - Please" +
                    " follow the format: yyyy-MM-dd");
            return Boolean.FALSE;
        }
        // Check if post code field contains a valid pc
        if(!isValidCode(codeField.getText())) {
            displayErrorAlert("Error Updating Record", "Invalid Input", "Wrong post code");
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    public Boolean updatePatientInfo() {
        String sex = "";
        if(maleBtn.isSelected()) {
            sex = maleBtn.getText();
        } else {
            sex = femaleBtn.getText();
        }

        String requestBody = "{\r\n  \"address\": \"" + addField.getText()+ "\"," +
                "\r\n  \"country\": \"" + countryBox.getSelectionModel().getSelectedItem()+ "\"," +
                "\r\n  \"dob\": \"" + dobField.getText()+ "\"," +
                "\r\n  \"email\": \"" + emailField.getText() + "\"," +
                "\r\n  \"fax\": \"" + faxField.getText() + "\"," +
                "\r\n  \"firstName\": \"" + fnameField.getText() + "\"," +
                "\r\n  \"homePhone\": \"" + hphoneField.getText() + "\"," +
                "\r\n  \"id\": \"" + pidField.getText() + "\"," +
                "\r\n  \"title\": \"" + titleBox.getSelectionModel().getSelectedItem() + "\"," +
                "\r\n  \"kinPhoneNumber\": \"" + kinField.getText() + "\"," +
                "\r\n  \"lastName\": \"" + lnameField.getText() + "\"," +
                "\r\n  \"maritalStatus\": \"" + statusBox.getSelectionModel().getSelectedItem() + "\","+
                "\r\n  \"mobilePhone\": \"" + mphoneField.getText() + "\"," +
                "\r\n  \"nextToKin\": \"" + kinField.getText() + "\"," +
                "\r\n  \"occupation\": \"" + occBox.getSelectionModel().getSelectedItem() + "\"," +
                "\r\n  \"officePhone\": \"" + ophoneField.getText() + "\"," +
                "\r\n  \"postcode\": \"" + codeField.getText() + "\"," +
                "\r\n  \"sex\": \"" + sex + "\"," +
                "\r\n  \"suburb\": \"" + subField.getText() + "\"\r\n}";
        return httpImpl.fetchPutRequest("http://visiderm.herokuapp.com/api/v1/patients/"+pidField.getText(), requestBody);

    }

    /**Name: displayAlert
     ** Purpose: This method helps to display alert in this screen
     * @param context: contains the information of the context section in alert dialog
     ** @return void
     */
    public void displayErrorAlert(String title, String header, String context){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
    }
    /**Name: isInteger
     ** Purpose: This method helps to check if a string is integer
     * @param string: contains string
     ** @return void
     */
    public static Boolean isInteger(String string) {
        if (string == null) {
            return Boolean.FALSE;
        }
        if (string.length() == 0) {
            return Boolean.FALSE;
        }
        int i = 0;
//        if (string.charAt(0) == '-') {
//            if (string.length() == 1) {
//                return Boolean.FALSE;
//            }
//            i = 1;
//        }
        if(string.contains("-")) {
            string = string.replaceAll("-","");
        }
        if(string.contains(".")) {
            string = string.replaceAll(".","");
        }
        for (; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c < '0' || c > '9') {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
    /**Name: isValidEmail
     ** Purpose: This method helps to check if a string is a valid email address
     * @param email: contains string
     ** @return void
     */
    public boolean isValidEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    /**Name: isValidCode
     ** Purpose: This method helps to check if a string is a valid post code
     * @param postCode: contains string
     ** @return void
     */
    public boolean isValidCode(String postCode) {
        String cPattern = "\\d{5}(-\\d{4})?";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(cPattern);
        java.util.regex.Matcher m = p.matcher(postCode);
        return m.matches();
    }
    /**Name: isValidEmail
     ** Purpose: This method helps to check if a string is a valid email address
     * @param dob: contains string
     ** @return void
     */
    public Boolean isValidDOB(String dob) {
        if(!dob.contains("-")) {
            return Boolean.FALSE;
        }
        String[] elements = dob.split("-");
        if(elements.length != 3) {
            return Boolean.FALSE;
        }
        if(!isInteger(elements[0]) || !isInteger(elements[1]) || !isInteger(elements[2])) {
            return Boolean.FALSE;
        }
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int date =  Calendar.getInstance().get(Calendar.DATE);
        int month =  Calendar.getInstance().get(Calendar.MONTH) + 1;
//        System.out.println(year);
//        System.out.println(date);
//        System.out.println(month);

        if(elements[0].length() != 4) {
            return Boolean.FALSE;
        }
        if(Integer.parseInt(elements[0]) > year) {
            return Boolean.FALSE;
        }
        if(Integer.parseInt(elements[0]) == year) {
            if (Integer.parseInt(elements[1]) > month) { return Boolean.FALSE; }
        }
        if(Integer.parseInt(elements[1]) == month) {
            if(Integer.parseInt(elements[2]) > date) { return Boolean.FALSE; }
        }

        return Boolean.TRUE;
        }

    private Node createVisitPage(int pageIndex) {
        int fromIndex = pageIndex * 10;
        int toIndex = Math.min(fromIndex + 10, visitList.size());
        visitTable.setItems(FXCollections.observableArrayList(visitList.subList(fromIndex, toIndex)));
        return visitTable;
    }

    //Receive message from record screen
    public void getSelectedPatient(Patient patient) {
        selectedPatient = patient;
    }
    //Receive message from record screen
    public void getVisitList(List<Visit> vlist) {
        visitList = vlist;
    }


}
