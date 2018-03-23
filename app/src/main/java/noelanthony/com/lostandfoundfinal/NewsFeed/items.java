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
    //private boolean status;

    public items(){}

    public items(String itemName, String dateSubmitted, String lastSeenLocation, String poster) {
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
    //public boolean isFound(){
      //  return status;
    //}
}
