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
    private boolean isSelected;
    private int approvalStatus;
    private String itemID;


    public items(){}

    public items(String itemName, String dateSubmitted, String lastSeenLocation, String poster,String status, String imageID,boolean isSelected,int approvalStatus,String itemID) {
        this.itemName = itemName;
        this.dateSubmitted = dateSubmitted;
        this.lastSeenLocation = lastSeenLocation;
        this.poster = poster;
        //this.status = status;
        this.imageID = imageID;
        this.isSelected = isSelected;
        this.approvalStatus = approvalStatus;
        this.itemID = itemID;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(int approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }
}


