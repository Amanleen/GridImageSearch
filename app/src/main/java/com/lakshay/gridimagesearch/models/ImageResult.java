package com.lakshay.gridimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by aman on 2/12/15.
 */
public class ImageResult implements Serializable {
    private String fullUrl;
    private String thumbUrl;
    private String title;
    private int width;
    private int height;

    public static ArrayList<ImageResult> fromArrayList(JSONArray resultArray){
        ArrayList<ImageResult> finalResultArray = new ArrayList<ImageResult>();
        try {
            for(int i=0; i<resultArray.length(); i++){
                JSONObject resultObj = (JSONObject)resultArray.get(i);
                ImageResult imgResult = new ImageResult();
                imgResult.setFullUrl(resultObj.getString("url"));
                imgResult.setTitle(resultObj.getString("title"));
                imgResult.setThumbUrl(resultObj.getString("tbUrl"));
                imgResult.setHeight(resultObj.getInt("height"));
                imgResult.setWidth(resultObj.getInt("width"));
                finalResultArray.add(imgResult);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return finalResultArray;
    }
    public int getWidth() {
        return width;
    }

    private void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    private void setHeight(int height) {
        this.height = height;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    private void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    private void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }
}
