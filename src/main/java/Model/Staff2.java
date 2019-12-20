package Model;

public class Staff2 {

    private long id;

    private String name;

    private String username;

    private String password;

    private boolean HasDoctorAccess;

    private Clinic2 clinic;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHasDoctorAccess() {
        return HasDoctorAccess;
    }

    public void setHasDoctorAccess(boolean hasDoctorAccess) {
        HasDoctorAccess = hasDoctorAccess;
    }

    public Clinic2 getClinic() {
        return clinic;
    }

    public void setClinic(Clinic2 clinic) {
        this.clinic = clinic;
    }
}
