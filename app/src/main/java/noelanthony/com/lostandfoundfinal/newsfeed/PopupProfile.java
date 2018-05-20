package noelanthony.com.lostandfoundfinal.newsfeed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import noelanthony.com.lostandfoundfinal.R;

/**
 * Created by Noel on 13/05/2018.
 */

public class PopupProfile extends Activity {
    private ImageView posterImageView;
    private TextView posternameTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popupprofile);

        posterImageView = findViewById(R.id.posterImageView);
        posternameTextView = findViewById(R.id.posternameTextView);
        String imageId = "";
        String posterName = "";

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));

        RequestOptions options = new RequestOptions();
        options.fitCenter();

        Intent intent = getIntent();
        if (null!= intent){
            imageId = intent.getStringExtra("poster_image");
            posterName = intent.getStringExtra("poster_name");
        }
        if(imageId==null){
            Glide.with(PopupProfile.this).load(R.mipmap.ic_noimage).apply(options).into(posterImageView);
        }else {

            Glide.with(PopupProfile.this).load(imageId).into(posterImageView); // IMAGE VIEW
        }
        if (posterName!=null){
            posternameTextView.setText(posterName);
        }
    }
}
