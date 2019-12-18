package Implementation;

import Model.Patient;
import Model.Visit;

import java.util.Map;

public class VisitImpl {


    /** Name: addPatient
     * Purpose: This method helps to add course.
     * @param visit - Contains all the details of staff such as id or name ,...
     * @param visitMap - Contains staff information in the staff map
     * @return String - Contains "OK" or "Error"
     */
    public Map<Long, Visit> addVisit(Visit visit, Map<Long, Visit> visitMap) {
        if(visitMap != null) {
            if(visitMap.containsKey(visit.getId())) {
                System.out.println("This patient already being stored");
            }
            //Add visit to map
            visitMap.put(visit.getId(), visit);
        }
        return visitMap;
    }
}
