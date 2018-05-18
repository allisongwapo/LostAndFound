package noelanthony.com.lostandfoundfinal;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Noel on 15/05/2018.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService{

    //this method will be called
    //when the token is generated
    @Override
    public void onTokenRefresh() {
      //  super.onTokenRefresh();

        //now we will have the token
        String token = FirebaseInstanceId.getInstance().getToken();

        //for now we are displaying the token in the log
        //copy it as this method is called only when the new token is generated
        //and usually new token is only generated when the app is reinstalled or the data is cleared
        //Log.d("FCM_TOKEN", token);

        Log.d("Firebase", "token "+ token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
}

