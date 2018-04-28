package noelanthony.com.lostandfoundfinal.NewsFeed;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Noel on 16/02/2018.
 */
@IgnoreExtraProperties
public class items {
    private String itemName;
    private String dateSubmitted;
    private String lastSeenLocation;
    private String poster;
    private String status;
    private String imageID;


    public items(){}

    public items(String itemName, String dateSubmitted, String lastSeenLocation, String poster,String status, String imageID) {
        this.itemName = itemName;
        this.dateSubmitted = dateSubmitted;
        this.lastSeenLocation = lastSeenLocation;
        this.poster = poster;
        //this.status = status;
        this.imageID = imageID;
    }

    public String getitemName() {
        return itemName;
    }

    public String getdateSubmitted() {
        return dateSubmitted;
    }

    public String getlastSeenLocation() {
        return lastSeenLocation;
    }

    public String getPoster() {
        return poster;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

}
