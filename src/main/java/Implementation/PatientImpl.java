package Implementation;

import Model.Patient;

import java.util.Map;

public class PatientImpl {

    /** Name: addPatient
     * Purpose: This method helps to add course.
     * @param patient - Contains all the details of staff such as id or name ,...
     * @param patientMap - Contains staff information in the staff map
     * @return String - Contains "OK" or "Error"
     */
    public Map<Integer, Patient> addPatient(Patient patient, Map<Integer, Patient> patientMap) {
        if(patientMap != null) {
            if(patientMap.containsKey(patient.getId())) {
                System.out.println("This patient already being stored");
            }
            //Add course to map
            patientMap.put(patient.getId(), patient);
        }
        return patientMap;
    }
}
