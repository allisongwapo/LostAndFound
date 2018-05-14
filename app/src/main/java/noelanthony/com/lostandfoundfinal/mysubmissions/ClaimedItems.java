package noelanthony.com.lostandfoundfinal.mysubmissions;

/**
 * Created by Noel on 08/05/2018.
 */

public class ClaimedItems {
    private String itemName;
    private String dateSubmitted;
    private String lastSeenLocation;
    private String poster;
    private String status;
    private String imageID;
    private boolean isSelected;
    private int approvalStatus;
    private String itemID;
    private String description;
    private String uid;


    public ClaimedItems(){}

    public ClaimedItems(String itemName, String dateSubmitted, String lastSeenLocation, String poster, String status, String imageID, boolean isSelected, int approvalStatus, String itemID, String description, String uid) {
        this.itemName = itemName;
        this.dateSubmitted = dateSubmitted;
        this.lastSeenLocation = lastSeenLocation;
        this.poster = poster;
        //this.status = status;
        this.imageID = imageID;
        this.isSelected = isSelected;
        this.approvalStatus = approvalStatus;
        this.itemID = itemID;
        this.description = description;
        this.uid = uid;
    }

    public String getitemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}




