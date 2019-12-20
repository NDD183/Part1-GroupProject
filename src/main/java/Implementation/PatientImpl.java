package Implementation;

import Model.Clinic;
import Model.Patient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class PatientImpl {

    /**
     * Name: addPatient
     * Purpose: This method helps to add course.
     *
     * @param patient    - Contains all the details of staff such as id or name ,...
     * @param patientMap - Contains staff information in the staff map
     * @return String - Contains "OK" or "Error"
     */
    public Map<Integer, Patient> addPatient(Patient patient, Map<Integer, Patient> patientMap) {
        if (patientMap != null) {
            if (patientMap.containsKey(patient.getId())) {
                System.out.println("Updating Patient Info");
            }
            //Add patient to map
            patientMap.put(patient.getId(), patient);
        }
        return patientMap;
    }

    public Patient getPatientByID(Integer pid, Map<Integer, Patient> patientMap) {

        if (patientMap != null) {
//            //Iterate through unit Map
//            Iterator<Map.Entry<Integer, Patient>> iterator = patientMap.entrySet().iterator();
//            //Iterate using while loop
//            while (iterator.hasNext()) {
//                Map.Entry<Integer, Patient> element = iterator.next();
            if (patientMap.containsKey(pid)) {
                return patientMap.get(pid);
            }
        }
        return null;
    }

    public ArrayList<Patient> getPatientByName(String name, Map<Integer, Patient> patientMap) {
        ArrayList<Patient> patientList = new ArrayList<>();
        if (patientMap != null) {
            //Iterate through patient Map
            Iterator<Map.Entry<Integer, Patient>> iterator = patientMap.entrySet().iterator();
            //Iterate using while loop
            while (iterator.hasNext()) {
                Map.Entry<Integer, Patient> element = iterator.next();
                if (element.getValue().getName().contains(name)) {
                    patientList.add(element.getValue());
                    System.out.println("added");
                }
            }
        }
        return patientList;
    }
}
