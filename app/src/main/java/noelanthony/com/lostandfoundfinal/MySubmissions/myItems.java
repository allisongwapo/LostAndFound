package noelanthony.com.lostandfoundfinal.MySubmissions;

/**
 * Created by Noel on 25/03/2018.
 */

public class myItems {
    private String itemName;
    private String dateSubmitted;
    private String lastSeenLocation;
    private String poster;

    //private boolean status;

    public myItems(){}

    public myItems(String itemName, String dateSubmitted, String lastSeenLocation, String poster) {
        this.itemName = itemName;
        this.dateSubmitted = dateSubmitted;
        this.lastSeenLocation = lastSeenLocation;
        this.poster = poster;
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
}
