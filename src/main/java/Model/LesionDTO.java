package Model;

import java.util.Set;

public class LesionDTO {

    // Define all required attributes of staff
    private int id;
    private String location;
    private String date;
    private String time;
    private Set<LesionHistoryDTO> lesionHistory;

    public LesionDTO(int id, String location, String date, String time) {
        this.id = id;
        this.location = location;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public Set<LesionHistoryDTO> getLesionHistory() {
        return lesionHistory;
    }

    public void setLesionHistory(Set<LesionHistoryDTO> lesionHistory) {
        this.lesionHistory = lesionHistory;
    }
}
