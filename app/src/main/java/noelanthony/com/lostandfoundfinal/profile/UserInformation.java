package noelanthony.com.lostandfoundfinal.profile;

/**
 * Created by Noel on 27/02/2018.
 */

public class UserInformation {
    private String name;
    private String image;
    private String email;
    private String datejoined;
    private int itemsreturned;
    private String idnumber;


    public UserInformation() {

    }

    public UserInformation(String name, String image, String email, String datejoined, int itemsreturned, String idnumber) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.datejoined = datejoined;
        this.itemsreturned = itemsreturned;
        this.idnumber = idnumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDatejoined() {
        return datejoined;
    }

    public void setDatejoined(String datejoined) {
        this.datejoined = datejoined;
    }

    public int getItemsreturned() {
        return itemsreturned;
    }

    public void setItemsreturned(int itemsreturned) {
        this.itemsreturned = itemsreturned;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }
}
