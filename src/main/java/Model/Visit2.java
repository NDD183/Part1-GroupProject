package Model;

public class Visit2 {

    private Long id;
    private String startDate;
    private String endDate;
    private String duration;
    private Clinic2 clinic;
    private Patient2 patient;
    private Staff2 staff;
    private String reason;
    private String note;

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    private String clinicName;
    private String patientFirstName;
    private String patientId;
    private String staffName;

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Clinic2 getClinic() {
        return clinic;
    }

    public void setClinic(Clinic2 clinic) {
        this.clinic = clinic;
    }

    public Patient2 getPatient() {
        return patient;
    }

    public void setPatient(Patient2 patient) {
        this.patient = patient;
    }

    public Staff2 getStaff() {
        return staff;
    }

    public void setStaff(Staff2 staff) {
        this.staff = staff;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
