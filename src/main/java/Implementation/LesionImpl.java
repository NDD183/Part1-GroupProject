package Implementation;

import Model.Clinic;
import Model.Lesion;

import java.util.Map;

public class LesionImpl {

    /** Name: addPatient
     * Purpose: This method helps to add course.
     * @param lesion - Contains all the details of staff such as id or name ,...
     * @param lesionMap - Contains staff information in the staff map
     * @return String - Contains "OK" or "Error"
     */
    public Map<Long, Lesion> addLesion(Lesion lesion, Map<Long, Lesion> lesionMap) {
        if(lesionMap != null) {
            if(lesionMap.containsKey(lesion.getId())) {
                System.out.println("This patient already being stored");
            }
            //Add course to map
            lesionMap.put(lesion.getId(), lesion);
        }
        return lesionMap;
    }

}
