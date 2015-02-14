package com.lakshay.gridimagesearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.lakshay.gridimagesearch.adapters.ImageResultsAdapter;
import com.lakshay.gridimagesearch.models.ImageResult;
import com.lakshay.gridimagesearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends Activity {

    private EditText etQuery;
    private GridView gvResult;
    private ArrayList<ImageResult> imageResultArray;
    private ImageResultsAdapter aImageResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpViews();
        imageResultArray = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this, imageResultArray);
        gvResult.setAdapter(aImageResults);

    }

    private void setUpViews() {
        etQuery = (EditText)findViewById(R.id.etQuery);
        gvResult = (GridView)findViewById(R.id.gvResult);
        gvResult.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
//                System.out.println("&&&&&&&&&&&&&& sending url ="+imageResultArray.get(position).getFullUrl());
                i.putExtra("url", imageResultArray.get(position).getFullUrl());
                i.putExtra("image_result", imageResultArray.get(position));
                startActivity(i);
            }
        });
    }

    private String formQuery(String query){
        String result = null;
        result = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+query+"&rsz=8&imgsz="+getPrefrenceValue("image_size_key","icon")+"&imgcolor="+getPrefrenceValue("color_filter_key","black")+"&imgtype="+getPrefrenceValue("image_type_key","face")+"&as_sitesearch="+getPrefrenceValue("site_filter_key","");


        return result;
    }

    private void executeQuery(){
        Button btnSearch = (Button)findViewById(R.id.btnSearch);
        String query = etQuery.getText().toString();

        String searchUrl = formQuery(query);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(searchUrl, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //System.out.println(response.toString());
//                Log.i("aman", response.toString());
                JSONArray resultArray = null;
                try {
                    resultArray = response.getJSONObject("responseData").getJSONArray("results");
                    imageResultArray.clear();
                    aImageResults.addAll(ImageResult.fromArrayList(resultArray));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Log.i("DEBUG",imageResultArray.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });
    }

    public void onSearchImage(View view){
        executeQuery();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            executeQuery();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(SearchActivity.this, SettingsActivity.class);
            startActivityForResult(i, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getPrefrenceValue(String key, String defaultVal){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(SearchActivity.this);
        return sharedPref.getString(key, defaultVal);
    }
}
