package noelanthony.com.lostandfoundfinal.NewsFeed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import noelanthony.com.lostandfoundfinal.R;

/**
 * Created by Noel on 13/05/2018.
 */

public class PopupImage extends Activity {
    private ImageView fullscreenImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popupwindow);

        fullscreenImageView = findViewById(R.id.fullscreenImageView);
        String imageId = "";

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));

        RequestOptions options = new RequestOptions();
        options.fitCenter();

        Intent intent = getIntent();
        if (null!= intent){
            imageId = intent.getStringExtra("image_id");
        }
        if(imageId==null){
            Glide.with(PopupImage.this).load(R.mipmap.ic_noimage).apply(options).into(fullscreenImageView);
        }else {

            Glide.with(PopupImage.this).load(imageId).into(fullscreenImageView); // IMAGE VIEW
        }
    }
}
