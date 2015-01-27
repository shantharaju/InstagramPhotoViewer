package com.example.sjayanna.instagramphotoviewer;

import android.net.http.AndroidHttpClient;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class InstagramViewerActivity extends ActionBarActivity {

    public static final String CLIENT_ID = "a40b742e64d947c0b8f2b9255348b4f0";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_viewer);
        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        // Create a data source
        photos = new ArrayList<>();
        // Bind data source to an adapter
        aPhotos = new InstagramPhotosAdapter(this, android.R.layout.simple_list_item_1, photos);
        // Attach adapter to an UI element, ListView
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);


        String popularUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.get(popularUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Log.i("DEBUG", response.toString());
                JSONArray photosJSON = null;

                // Clear photos array before populating the data every time
                photos.clear();
                try {
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); ++i) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();

                        // Populate attributes of photo from JSON response
                        photo.userName = photoJSON.getJSONObject("user").getString("username");
                        // Sometimes caption could be null
                        if (photoJSON.optJSONObject("caption") != null) {
                            photo.caption = photoJSON.optJSONObject("caption").getString("text");
                        }
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photo.userProfilePhotoUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                        photo.createdTime = photoJSON.getLong("created_time");

                        Log.i("DEBUG", photo.userName + " " + photo.createdTime + " " + photo.userProfilePhotoUrl);
                        // Populate photos
                        photos.add(photo);
                    }
                    // Notify adapter that data source has changed
                    aPhotos.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_instagram_viewer, menu);
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
