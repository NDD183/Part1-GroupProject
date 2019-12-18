package Implementation;

import Model.Clinic;
import Model.Patient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ClinicImpl {
    /** Name: addPatient
     * Purpose: This method helps to add course.
     * @param clinic - Contains all the details of staff such as id or name ,...
     * @param clinicMap - Contains staff information in the staff map
     * @return String - Contains "OK" or "Error"
     */
    public Map<Long, Clinic> addClinic(Clinic clinic, Map<Long, Clinic> clinicMap) {
        if(clinicMap != null) {
            if(clinicMap.containsKey(clinic.getId())) {
                System.out.println("This patient already being stored");
            }
            //Add course to map
            clinicMap.put(clinic.getId(), clinic);
        }
        return clinicMap;
    }

    public ArrayList<String> getClinicNameID(Map<Long, Clinic> clinicMap) {
        ArrayList<String> cnameList = new ArrayList<>();
        if(clinicMap != null) {
            //Iterate through unit Map
            Iterator<Map.Entry<Long, Clinic>> iterator = clinicMap.entrySet().iterator();
            //Iterate using while loop
            while (iterator.hasNext()) {
                Map.Entry<Long, Clinic> element = iterator.next();
                cnameList.add(element.getValue().getId() + " - " +element.getValue().getName());
            }
        }
        return cnameList;
    }
}
