package Implementation;

import Model.Lesion;
import Model.LesionHistory;

import java.util.Map;

public class LesionHistoryImpl {

    /** Name: addPatient
     * Purpose: This method helps to add course.
     * @param lesionHistory - Contains all the details of staff such as id or name ,...
     * @param lesionHistoryMap - Contains staff information in the staff map
     * @return String - Contains "OK" or "Error"
     */
    public Map<Long, LesionHistory> addLesionHistory(LesionHistory lesionHistory, Map<Long, LesionHistory> lesionHistoryMap) {
        if(lesionHistoryMap != null) {
            if(!lesionHistoryMap.containsKey(lesionHistory.getId())) {
                System.out.println("Updating Lesion History Info");
            }
            //Add lesion history to map
            lesionHistoryMap.put(lesionHistory.getId(), lesionHistory);
        }
        return lesionHistoryMap;
    }


}
