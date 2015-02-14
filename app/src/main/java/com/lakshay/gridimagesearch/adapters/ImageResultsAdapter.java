package com.lakshay.gridimagesearch.adapters;

import android.content.Context;
import android.text.Html;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lakshay.gridimagesearch.R;
import com.lakshay.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by aman on 2/12/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {


    public ImageResultsAdapter(Context context, List<ImageResult> images) {
        super(context,android.R.layout.simple_list_item_1 , images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageInfo = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
        }
        TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
        ImageView ivImage = (ImageView)convertView.findViewById(R.id.ivImage);
        ivImage.setImageResource(0);
        Picasso.with(getContext()).load(imageInfo.getThumbUrl()).placeholder(R.drawable.app_icon).into(ivImage);
        tvTitle.setText(Html.fromHtml(imageInfo.getTitle()));
        return convertView;
    }

}
