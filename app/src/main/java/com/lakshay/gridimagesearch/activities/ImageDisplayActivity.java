package com.lakshay.gridimagesearch.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.lakshay.gridimagesearch.R;
import com.lakshay.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.io.Serializable;


public class ImageDisplayActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        getActionBar().hide();
        String url = getIntent().getStringExtra("url");
        ImageResult imageResult = (ImageResult) getIntent().getSerializableExtra("image_result");
        System.out.println("****************** url ="+url);
        ImageView ivFullImage = (ImageView)findViewById(R.id.ivFullImage);
        Picasso.with(getBaseContext()).load(url).placeholder(R.drawable.app_icon).into(ivFullImage);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
