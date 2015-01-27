package com.example.sjayanna.instagramphotoviewer;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by sjayanna on 1/25/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramPhotosAdapter(Context context, int resource, List<InstagramPhoto> photos) {
        super(context, resource, photos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Algo:
        // 1. Get Item position
        // 2. Inflate layout, if convertView is null
        // 3. Get reference to UI elements within the layout view we are generating
        // 4. Use Picasso library to asynchronously load the image
        // 4. Udpate remaining UI elements
        // 5. Return re-updated convertView for display

        InstagramPhoto photo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        ImageView ivUserProfilePhoto = (ImageView) convertView.findViewById(R.id.ivUserProfilePhoto);
        TextView tvTimeStamp = (TextView) convertView.findViewById(R.id.tvTimeStamp);
        TextView tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);

        String formattedText = String.format("<font size='14' color='#152843'><b> %s </b> </font>", photo.userName);

        tvUserName.setText(Html.fromHtml(formattedText));
        tvCaption.setText(photo.caption);

        formattedText = String.format("♥ <font size='12' color='blue'> %d </font><b> likes</b>", photo.likesCount);
        tvLikesCount.setText(Html.fromHtml(formattedText));

        ivPhoto.getLayoutParams().height = photo.imageHeight;
        ivUserProfilePhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.userProfilePhotoUrl).into(ivUserProfilePhoto);

        formattedText = String.format("🕐 %s", DateUtils.getRelativeTimeSpanString(photo.createdTime * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
        tvTimeStamp.setText(formattedText);
        // Reset imageView
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);
        return convertView;
    }
}
