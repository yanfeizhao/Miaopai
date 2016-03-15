package com.qst.fly.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;

import com.qst.fly.R;

public class CameraAlbumDetailActivity extends Activity {
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {

    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_albumdetail);
    	
    	
    }
}
