package Model;

public class Patient2 {

    // Define all required attributes of staff
    // This variable present for unique staff ID
    private int id;
    // This variable present for the staff name
    private String name;
    private String title;
    private String sex;
    private String maritalStatus;
    private String dob;
    private String address;
    private String sub;
    private String postcode;
    private String country;
    private String hphone;
    private String ophone;
    private String mphone;
    private String fax;
    private String email;
    private String occupation;
    private String ntk;
    private String kphone;

    public Patient2(int id, String name, String title, String sex, String maritalStatus, String dob, String address, String sub, String
                   postcode, String country, String hphone, String ophone, String mphone, String fax, String email, String
                   occupation, String ntk, String kphone) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.sex = sex;
        this.maritalStatus = maritalStatus;
        this.dob = dob;
        this.address = address;
        this.sub = sub;
        this.postcode = postcode;
        this.country = country;
        this.hphone = hphone;
        this.ophone = ophone;
        this.mphone = mphone;
        this.fax = fax;
        this.email = email;
        this.occupation = occupation;
        this.ntk = ntk;
        this.kphone = kphone;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getNtk() {
        return ntk;
    }

    public void setNtk(String ntk) {
        this.ntk = ntk;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getHphone() {
        return hphone;
    }

    public void setHphone(String hphone) {
        this.hphone = hphone;
    }

    public String getOphone() {
        return ophone;
    }

    public void setOphone(String ophone) {
        this.ophone = ophone;
    }

    public String getMphone() {
        return mphone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMphone(String mphone) {
        this.mphone = mphone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getKphone() {
        return kphone;
    }

    public void setKphone(String kphone) {
        this.kphone = kphone;
    }
}
