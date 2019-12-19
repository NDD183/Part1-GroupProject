package Model;

import javafx.scene.image.ImageView;

import java.util.Set;

public class Lesion {

    // Define all required attributes of staff
    private Long id;
    private Visit visit;
    private String location;
    private String status;
    private Set<LesionHistory> lesionHistory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<LesionHistory> getLesionHistory() {
        return lesionHistory;
    }

    public void setLesionHistory(Set<LesionHistory> lesionHistory) {
        this.lesionHistory = lesionHistory;
    }
}
