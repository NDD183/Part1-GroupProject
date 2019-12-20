package Controller;


import Implementation.HttpImpl;
import Implementation.PatientImpl;
import Implementation.VisitImpl;
import Model.Clinic;
import Model.Patient;
import Model.Staff;
import Model.Visit;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class created to handle the logic when user interact with record UI (screen 1)
 *
 * @author s3634096
 *
 */
public class RecordController implements Initializable {

    // Initialize UI component
    @FXML
    Label hosNameLab;
    @FXML
    Label dateLab;
    @FXML
    Label accLab;
    @FXML
    Label searchLab;
    @FXML
    Label editPatientLab;
    @FXML
    Label editVisitLab;
    @FXML
    TextField searchField;
    @FXML
    ComboBox<String> typeBox;
    @FXML
    TableView<Patient> recordTable;
    @FXML
    private TableColumn<Patient, Integer> idColumn;
    @FXML
    private TableColumn<Patient, String> nameColumn;
    @FXML
    private TableColumn<Patient, String> addColumn;
    @FXML
    private TableColumn<Patient, String> sexColumn;
    @FXML
    private TableColumn<Patient, Integer> phoneColumn;
    @FXML
    private TableColumn<Patient, String> dobColumn;
    @FXML
    TableView<Visit> visitTable;
    @FXML
     TableColumn<Visit, Long> orderColumn;
    @FXML
     TableColumn<Visit, String> docColumn;
    @FXML
     TableColumn<Visit, String> clinicColumn;
    @FXML
     TableColumn<Visit, String> dateColumn;
    @FXML
     TableColumn<Visit, String> timeColumn;
    @FXML
     TableColumn<Visit, String> reasonColumn;
    @FXML
    Label nameLab;
    @FXML
    Label logoutLab;
    @FXML
    Label startDateLab;
    @FXML
    Label endDateLab;
    @FXML
    Pagination recordPage;
    @FXML
    Button searchBtn;
    @FXML
    Pagination visitPage;

    // Initialize required variables
    static PatientImpl patientImpl = new PatientImpl();
    static VisitImpl visitImpl = new VisitImpl();
    private HttpImpl httpImpl = new HttpImpl();
    private Map<Integer, Patient> patientMap =  new HashMap<>();
    private Map<Long, Visit> visitMap =  new HashMap<>();
    private Map<Integer, Patient> searchMap =  new HashMap<>();
    static int responseCode;
    static long loginClinicID;
    static String logServer;
    private ObservableList<Patient> patientData;
    private  List<Patient> patientList ;
    private ObservableList<Visit> visitData;
    private  List<Visit> visitList ;
    private ScreenController screenController = new ScreenController();
    private Patient selectedPatient;
    private Visit selectedVisit;
   // private final static int dataSize = 100;
    private final static int prowsPerPage = 10;
    private final static int vrowsPerPage = 5;
   // static  Set<Patient> patientSet = new HashSet<>();
    /**Name: initialize
     **Event: When the patientRecord.fxml is loaded
     **Purpose: Setting UI component before showing to user
     **Passed: All UI components named correctly
     **Returns: void
     **Input: void // Output: The record  UI -  the patientRecord.fxml is loaded successfully
     **Effect: The method help user to additionally config UI
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(loginClinicID);
        // Set hover when moving mouse to specific label
        accLab.setCursor(Cursor.HAND);
        searchLab.setCursor(Cursor.HAND);
        editPatientLab.setCursor(Cursor.HAND);
        editVisitLab.setCursor(Cursor.HAND);
        logoutLab.setCursor(Cursor.HAND);
        // Additional configure label
        editPatientLab.setDisable(true);
        editVisitLab.setDisable(true);
        // Set value for comboBox
        typeBox.getItems().addAll("ID","Name");
        typeBox.setValue("ID");
        // Set value for current date label
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int date =  Calendar.getInstance().get(Calendar.DATE);
        int month =  Calendar.getInstance().get(Calendar.MONTH) + 1;
        int dow = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        String[] dayOfWeek = {"SUN", "MON", "TUE", "WED", "THU", "FRI","SAT"};
        dateLab.setText(dayOfWeek[dow-1]+" " +date+"/" +month+ "/" + year);
        // Configure table column
        // Set text alignment for each column in record table
        nameColumn.setStyle("-fx-alignment: CENTER;");
        idColumn.setStyle("-fx-alignment: CENTER;");
        sexColumn.setStyle("-fx-alignment: CENTER;");
        dobColumn.setStyle("-fx-alignment: CENTER;");
        addColumn.setStyle("-fx-alignment: CENTER;");
        phoneColumn.setStyle("-fx-alignment: CENTER;");
        // Set text alignment for each column in visit table
        orderColumn.setStyle("-fx-alignment: CENTER;");
        dateColumn.setStyle("-fx-alignment: CENTER;");
        timeColumn.setStyle("-fx-alignment: CENTER;");
        dobColumn.setStyle("-fx-alignment: CENTER;");
        reasonColumn.setStyle("-fx-alignment: CENTER;");
        // Set record table behaviour when user click on each row
        recordTable.setRowFactory(tv -> {
            TableRow<Patient> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty()) ) {
                    selectedPatient = row.getItem();
                    System.out.println(selectedPatient.getName());
                    nameLab.setText(selectedPatient.getName());
                  // System.out.println(fetchGetRequest("http://visiderm.herokuapp.com/api/v1/patients/" +selectedPatient.getId()+ "/visits"));
                    loadData("visit", httpImpl.fetchGetRequest("http://visiderm.herokuapp.com/api/v1/patients/" +selectedPatient.getId()+ "/visits"));
                    reloadTable("visit", null);
                    visitMap = null;
                }
            });
            return row ;
        });
        // Set visit table behaviour when user double click on each row
        visitTable.setRowFactory(tv -> {
            TableRow<Visit> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty()) ) {
                    selectedVisit = row.getItem();
                    System.out.println(selectedVisit.getStartDate());
                    if(logServer.equals("Head Doctor")) {
                        editVisitLab.setDisable(false);
                    }
                }
            });
            return row ;
        });

        // Perform different function based on response code
        // 101: fetch again all patient data  - 202: use again the last fetch data
        //if(responseCode == 101){
            // Add data to map
            // Get all patient: http://visiderm.herokuapp.com/api/v1/patients
            // Get patient by clinic: http://visiderm.herokuapp.com/api/v1/clinic/1/patient
           loadData("patient", httpImpl.fetchGetRequest("http://visiderm.herokuapp.com/api/v1/clinic/"+loginClinicID+"/patients"));
      //  }
        // Load Record to UI
            reloadTable("patient", patientMap);

            accLab.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    screenController.closeScreen((Stage) accLab.getScene().getWindow());
                    screenController.openScreen("map");
                }
            });
    }


    // ALL EVENT FUNCTIONS

    /**Name: initialize
     **Event: When the patientRecord.fxml is loaded
     **Purpose: Setting UI component before showing to user
     **Passed: All UI components named correctly
     **Returns: void
     **Input: void // Output: The record  UI -  the patientRecord.fxml is loaded successfully
     **Effect: The method help user to additionally config UI
     */
    public void accessClicked(MouseEvent mouseEvent) {
        System.out.println("access clicked");
    }
    /**Name: initialize
     **Event: When the patientRecord.fxml is loaded
     **Purpose: Setting UI component before showing to user
     **Passed: All UI components named correctly
     **Returns: void
     **Input: void // Output: The record  UI -  the patientRecord.fxml is loaded successfully
     **Effect: The method help user to additionally config UI
     */
    public void searchClicked(MouseEvent mouseEvent) {
        screenController.closeScreen((Stage) searchLab.getScene().getWindow());
        screenController.openScreen("lesionsSearch");
        System.out.println("search clicked");
    }
    /**Name: initialize
     **Event: When the patientRecord.fxml is loaded
     **Purpose: Setting UI component before showing to user
     **Passed: All UI components named correctly
     **Returns: void
     **Input: void // Output: The record  UI -  the patientRecord.fxml is loaded successfully
     **Effect: The method help user to additionally config UI
     */
    public void searchPressed(ActionEvent actionEvent)  {
        ArrayList<Patient> searchedPatients = new ArrayList<>();
        if(!searchField.getText().equals("")) {
            if(typeBox.getSelectionModel().getSelectedItem().equals("ID")) {
//               String patientInfo = httpImpl.fetchGetRequest("http://visiderm.herokuapp.com/api/v1/patients/"+searchField.getText());
//                if(!patientInfo.equals("Error")) {
//                    JsonObject patient = new JsonParser().parse(patientInfo).getAsJsonObject();
//
//                    String[] patientInfos = httpImpl.exactHttpResponse("patient", patient);
//                    addPatient("search", Integer.parseInt(patientInfos[0]), patientInfos[1]+" "+patientInfos[2],patientInfos[3],
//                            patientInfos[4],patientInfos[5], patientInfos[6],patientInfos[7],patientInfos[8], patientInfos[9],
//                            patientInfos[10], patientInfos[11] ,patientInfos[12], patientInfos[13],patientInfos[14],
//                            patientInfos[15],patientInfos[16], patientInfos[17], patientInfos[18]);
//                    reloadTable("patient",searchMap);
//                }

            searchedPatients.add(patientImpl.getPatientByID(Integer.parseInt(searchField.getText()), patientMap));
            }
            if(typeBox.getSelectionModel().getSelectedItem().equals("Name")){
                searchedPatients = patientImpl.getPatientByName(searchField.getText(), patientMap);
            }
            recordTable.setItems(null);
            recordTable.setItems(FXCollections.observableArrayList(searchedPatients));
        } else {
            reloadTable("patient",patientMap);
//            displayErrorAlert("Message from system", "Invalid ID/Name", "Please enter a valid patient ID " +
//                    "or name to use search function");
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
    public void editRecordClicked(MouseEvent mouseEvent) {
        UpdateRecordController updateController = new UpdateRecordController();
        if(selectedPatient != null) {
            screenController.closeScreen((Stage) hosNameLab.getScene().getWindow());
            updateController.getSelectedPatient(selectedPatient);
            System.out.println(visitList.get(0).getId());
            updateController.getVisitList(visitList);
            screenController.openScreen("editRecord");
        } else {
            displayErrorAlert("Message from system", "Error", "Please select one specific patient"
                                  +" showed in above table to edit his/her record");
        }
        System.out.println("edit record clicked");
    }
    /**Name: initialize
     **Event: When the patientRecord.fxml is loaded
     **Purpose: Setting UI component before showing to user
     **Passed: All UI components named correctly
     **Returns: void
     **Input: void // Output: The record  UI -  the patientRecord.fxml is loaded successfully
     **Effect: The method help user to additionally config UI
     */
    public void editVisitClicked(MouseEvent mouseEvent) {
        screenController.closeScreen((Stage) editPatientLab.getScene().getWindow());
        VisitController.selectedVisitID = Math.toIntExact(selectedVisit.getId());
        VisitController.selectedPatientID = Math.toIntExact(selectedPatient.getId());
        screenController.openScreen("visit");
        System.out.println("edit visit clicked");
    }

    public void logClicked(MouseEvent mouseEvent) {
        screenController.closeScreen((Stage) searchLab.getScene().getWindow());
        screenController.openScreen("login");
    }


// ALL SUPPORT FUNCTIONS

    /**Name: initialize
     **Event: When the patientRecord.fxml is loaded
     **Purpose: Setting UI component before showing to user
     **Passed: All UI components named correctly
     **Returns: void
     **Input: void // Output: The record  UI -  the patientRecord.fxml is loaded successfully
     **Effect: The method help user to additionally config UI
     */

    public void loadData(String type, String response) {

        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        JsonArray contentResponse = (JsonArray) jsonObject.get("content");
        //  System.out.println(contentResponse.size());
        // System.out.println(contentResponse.get(10).getAsJsonObject().get("id"));

        for (int i = 0; i < contentResponse.size(); i++) {
            String[] infos = httpImpl.exactHttpResponse(type, contentResponse.get(i).getAsJsonObject());

            if (type.equals("patient")) {
                int pid;
                try {
                    pid = Integer.parseInt(infos[0]);
                } catch (NumberFormatException nfe) {
                    System.out.println(nfe.toString());
                    break;
                }
                addPatient("all", pid, infos[1] + " " + infos[2], infos[3],
                        infos[4], infos[5], infos[6], infos[7], infos[8], infos[9],
                        infos[10], infos[11], infos[12], infos[13], infos[14],
                        infos[15], infos[16], infos[17], infos[18]);
            }
            if(type.equals("visit")) {

                Clinic clinic = new Clinic(Long.parseLong(String.valueOf(contentResponse.get(i).getAsJsonObject().get("clinic").getAsJsonObject().get("id"))),
                        String.valueOf(contentResponse.get(i).getAsJsonObject().get("clinic").getAsJsonObject().get("city")),
                        String.valueOf(contentResponse.get(i).getAsJsonObject().get("clinic").getAsJsonObject().get("country")),
                        String.valueOf(contentResponse.get(i).getAsJsonObject().get("clinic").getAsJsonObject().get("name")));

                String[] patientInfo = httpImpl.exactHttpResponse("patient", contentResponse.get(i).getAsJsonObject().get("patient").getAsJsonObject());
                Patient patient = addPatient("", Integer.parseInt(patientInfo[0]), patientInfo[1] + " " + patientInfo[2], patientInfo[3],
                        patientInfo[4], patientInfo[5], patientInfo[6], patientInfo[7], patientInfo[8], patientInfo[9],
                        patientInfo[10], patientInfo[11], patientInfo[12], patientInfo[13], patientInfo[14],
                        patientInfo[15], patientInfo[16], patientInfo[17], patientInfo[18]);

                Staff staff = new Staff();
                staff.setName(String.valueOf(contentResponse.get(i).getAsJsonObject().get("staff").getAsJsonObject().get("name")));
                staff.setId(Long.parseLong(String.valueOf(contentResponse.get(i).getAsJsonObject().get("staff").getAsJsonObject().get("id"))));
                staff.setHasDoctorAccess(Boolean.parseBoolean(String.valueOf(contentResponse.get(i).getAsJsonObject().get("staff").getAsJsonObject().get("hasDoctorAccess"))));
                staff.setUsername(String.valueOf(contentResponse.get(i).getAsJsonObject().get("staff").getAsJsonObject().get("username")));
                staff.setPassword(String.valueOf(contentResponse.get(i).getAsJsonObject().get("staff").getAsJsonObject().get("password")));
                staff.setClinic(clinic);

                long vid;
                try {
                    vid = Long.parseLong(infos[0]);
                } catch (NumberFormatException nfe) {
                    System.out.println(nfe.toString());
                    break;
                }
                addVisit(vid, infos[1], infos[2], clinic, patient, staff, infos[3], infos[4]);
            }
        }
    }

    /**Name: addPatient
     ** Purpose: This method helps to add staff to system from the provided file (execute in first run of application only)
     ** @return String - contains "OK": successfully add staff info
     */
    public Patient addPatient(String type, int id, String name, String title, String sex, String maritalStatus, String dob,
                              String address, String sub, String postcode, String country, String hphone, String ophone,
                              String mphone,String fax, String email, String occupation, String ntk, String kphone){
        Patient patient = new Patient(id, name, title, sex, maritalStatus, dob, address, sub, postcode, country, hphone,
                ophone, mphone, fax, email, occupation, ntk, kphone);
//        patient.setId(id);
//        patient.setName(name);
//        patient.setSex(sex);
//        patient.setMaritalStatus(maritalStatus);
//        patient.setDob(dob);
//        patient.setSub(sub);
//        patient.setPostcode(postcode);
//        patient.setCountry(country);
//        patient.setHphone(hphone);
//        patient.setOphone(ophone);
//        patient.setMphone(mphone);
//        patient.setFax(fax);
//        patient.setEmail(email);
//        patient.setOccupation(occupation);
//        patient.setNtk(ntk);
//        patient.setKphone(kphone);
//        patient.setAddress(address);
        if(type.equals("search")) {
            if (searchMap == null) {
                searchMap = patientImpl.addPatient(patient, new HashMap<>());
            } else {
                searchMap = patientImpl.addPatient(patient, searchMap);
            }
        }
        if(type.equals("all")){
            if (patientMap == null) {
                patientMap = patientImpl.addPatient(patient, new HashMap<>());
            } else {
                patientMap = patientImpl.addPatient(patient, patientMap);
            }
        }
        //   patientSet.add(patient);
        return patient;
    }

    /**Name: addPatient
     ** Purpose: This method helps to add staff to system from the provided file (execute in first run of application only)
     ** @return String - contains "OK": successfully add staff info
     */
    public String addVisit(long id, String startDate, String endDate, Clinic clinic, Patient patient, Staff staff, String reason, String note){

        // Transform start date to date and time
        String[] startDateArr = startDate.split("T");
        String[] endDateArr = endDate.split("T");
        String duration = startDateArr[1].substring(0, 8) + " - " +endDateArr[1].substring(0, 8);

        // Create new visit model
        Visit visit = new Visit();

        visit.setId(id);
        visit.setStartDate(startDateArr[0]);
        visit.setEndDate(endDateArr[0]);
        visit.setClinic(clinic);
        visit.setPatient(patient);
        visit.setStaff(staff);
        visit.setReason(reason);
        visit.setNote(note);
        visit.setDuration(duration);

        if (visitMap == null) {
            visitMap = visitImpl.addVisit(visit, new HashMap<>());
        } else {
            visitMap = visitImpl.addVisit(visit, visitMap);
        }

        return "OK";
    }
    /**Name: loadRecord
     ** Purpose: This method helps to load all patient record info to UI
     */
    public void reloadTable(String type, Map<Integer, Patient> map)  {
        if(type.equals("patient")) {
            if (map != null) {
                // Transform map to array list
                patientList = new ArrayList<>(map.values());

                // Add list of staff to the observableArrayList
                patientData = FXCollections.observableArrayList(patientList);


                // Set column in array to present as different attributes of staff
                nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                sexColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));
                dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
                addColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
                phoneColumn.setCellValueFactory(new PropertyValueFactory<>("mphone"));

                // Set the created list to the staff table
                recordTable.setItems(null);
                recordTable.setItems(this.patientData);

                // Applying pagination to record table
                recordPage.setCurrentPageIndex(0);
                recordPage.setPageCount(patientList.size() / prowsPerPage + 1);
                recordPage.setPageFactory(this::createPatientPage);
            }
        }
        if(type.equals("visit")) {

            if(visitMap != null) {
                // Transform map to array list
                visitList = new ArrayList<>(visitMap.values());

                // Find the start date and end date of list of visit
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = new Date();
                Date endDate = new Date();
                int startIndex = 0;
                int endIndex = 0;
                try {
                    startDate = dateFormat.parse(visitList.get(0).getStartDate());
                    endDate = dateFormat.parse(visitList.get(0).getStartDate());
                } catch (ParseException e) { e.printStackTrace();}

                for(int i =0; i < visitList.size(); i++) {
                    try {
                        Date currentDate = dateFormat.parse(visitList.get(i).getStartDate());
                        if(startDate.compareTo(currentDate) > 0) {
                            startIndex = i;
                            startDate = currentDate;
                        }
                        if(endDate.compareTo(currentDate) < 0) {
                            endIndex = i;
                            endDate = currentDate;
                        }
                    } catch (ParseException e) { e.printStackTrace(); }
                }

                // Insert value for start and end date label
                startDateLab.setText(visitList.get(startIndex).getStartDate());
                endDateLab.setText(visitList.get(endIndex).getStartDate());

                // Add list of staff to the observableArrayList
                visitData =  FXCollections.observableArrayList(visitList);

                // Set column in array to present as different attributes of staff
                orderColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                dateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
                timeColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
                reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));

                // Only insert doctor name and clinic name to the table column
                docColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Visit, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Visit, String> param) {
                        return new SimpleStringProperty(param.getValue().getStaff().getName());
                    }
                });
                clinicColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Visit, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Visit, String> param) {
                        return new SimpleStringProperty(param.getValue().getClinic().getName());
                    }
                });

                // Set the created list to the staff table
                visitTable.setItems(null);
                visitTable.setItems(this.visitData);

                // Applying pagination to record table
                visitPage.setCurrentPageIndex(0);
                visitPage.setPageCount(visitList.size() / vrowsPerPage + 1);
                visitPage.setPageFactory(this::createVisitPage);
            }
            if(logServer.equals("Head Doctor")) {
                editPatientLab.setDisable(false);
            }
        }
    }
    /**Name: createPatientPage
     ** Purpose: This method helps to display alert in this screen
     * @param pageIndex: contains the information of the context section in alert dialog
     ** @return void
     */
    private Node createPatientPage(int pageIndex) {
        int fromIndex = pageIndex * prowsPerPage;
        int toIndex = Math.min(fromIndex + prowsPerPage, patientList.size());
        recordTable.setItems(FXCollections.observableArrayList(patientList.subList(fromIndex, toIndex)));
        return recordTable;
    }
    /**Name: displayAlert
     ** Purpose: This method helps to display alert in this screen
     * @param pageIndex: contains the information of the context section in alert dialog
     ** @return void
     */
    private Node createVisitPage(int pageIndex) {
        int fromIndex = pageIndex * vrowsPerPage;
        int toIndex = Math.min(fromIndex + vrowsPerPage, visitList.size());
        visitTable.setItems(FXCollections.observableArrayList(visitList.subList(fromIndex, toIndex)));
        return visitTable;
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


    //Receive message from scene 2
    public void getLoadCode(int code) {
        responseCode = code;
    }
    //Receive message from scene 2
    public void getServer(String server) {
        logServer = server;
    }
    //Receive message from login screen
    public void getClinicID(String cid) {
        loginClinicID = Long.parseLong(cid);
    }


}
