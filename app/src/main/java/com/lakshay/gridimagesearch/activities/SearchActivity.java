package com.lakshay.gridimagesearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
    private Button loadMoreButton;

    private int nextPage = 1;
    private int nextStart = 0;
    private static final int MAX_PAGES = 8;

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
        etQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // disable load more button when text changes
                loadMoreButton.setEnabled(false);
            }
        });
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
        loadMoreButton = (Button) findViewById(R.id.btnLoadMore);
        loadMoreButton.setEnabled(false);
    }

    private String formQuery(String query, boolean loadMore){
        String result = null;
        result = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+query+"&rsz=8&imgsz="+getPrefrenceValue("image_size_key","icon")+"&imgcolor="+getPrefrenceValue("color_filter_key","black")+"&imgtype="+getPrefrenceValue("image_type_key","face")+"&as_sitesearch="+getPrefrenceValue("site_filter_key","")+"&start="+nextStart;
        return result;
    }

    private void executeQuery(final boolean loadMore){
        Button btnSearch = (Button)findViewById(R.id.btnSearch);
        String query = etQuery.getText().toString();

        String searchUrl = formQuery(query, loadMore);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(searchUrl, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //System.out.println(response.toString());
//                Log.i("aman", response.toString());
                JSONArray resultArray = null;
                JSONObject responseData = null;
                JSONObject cursorObject = null;
                try {
                    responseData = response.getJSONObject("responseData");
                    resultArray = responseData.getJSONArray("results");
                    cursorObject = responseData.getJSONObject("cursor");
                    JSONArray pages = cursorObject.getJSONArray("pages");
                    if (nextPage < MAX_PAGES && nextPage < pages.length()) {
                        nextStart = pages.getJSONObject(nextPage).getInt("start");
                        nextPage++;
                        loadMoreButton.setEnabled(true);
                    } else {
                        loadMoreButton.setEnabled(false);
                    }
                    if (!loadMore) {
                        imageResultArray.clear();
                    }
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
        executeQuery(false);
    }

    public void onLoadMore(View view) {
        executeQuery(true);
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
            // reset query after settings changed
            nextPage = 1;
            nextStart = 0;
            executeQuery(false);
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
