package Model;

public class LesionHistoryDTO {

    private String color;
    private String date;
    private String diagnoses;
    private int id;
    private String note;
    private String size;
    
    
    
//    private Long id;
//
//    private Date date;
//
//    private String note;
//
//    private String size;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }
//
//    public String getNote() {
//        return note;
//    }
//
//    public void setNote(String note) {
//        this.note = note;
//    }
//
//    public String getSize() {
//        return size;
//    }
//
//    public void setSize(String size) {
//        this.size = size;
//    }

    public LesionHistoryDTO(String color, String date, String diagnoses, int id, String note,
                            String size) {
        this.color = color;
        this.date = date;
        this.diagnoses = diagnoses;
        this.id = id;
        this.note = note;
        this.size = size;
    }
}
