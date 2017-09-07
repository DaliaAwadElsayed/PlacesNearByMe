package com.example.dalia.placesnearbyme;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dalia.placesnearbyme.Adapter.NearByAdapter;
import com.example.dalia.placesnearbyme.DataModel.NearByDataModel;
import com.example.dalia.placesnearbyme.DataModel.Venue;
import com.example.dalia.placesnearbyme.Location.GPSTracker;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    ListView listview;
    EditText search;
    NearByAdapter nearAdapter;
    ImageView imagesearch;
    List<Venue> venueArrayList;
    String latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String tag = "", message = "";
        Log.d(tag, message);
        listview = (ListView) findViewById(R.id.ls);
        search = (EditText) findViewById(R.id.editTextsearchid);

        GPSTracker gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            latitude = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());
        }

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        final Gson gson = new Gson();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String url = "https://api.foursquare.com/v2/venues/search?ll="+latitude+","+longitude+"&query=" + editable + "&oauth_token=P4GY10EVNBZCEBC5EM1DSKOWX2LBXFRZA5WPTI02RMNQZAE4&v=20170829&radius=1000000";

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                               // Toast.makeText(MainActivity.this, latitude+longitude, Toast.LENGTH_SHORT).show();
                                NearByDataModel nearByDataModel = gson.fromJson(response, NearByDataModel.class);
                                venueArrayList = nearByDataModel.getResponse().getVenues();
                                nearAdapter = new NearByAdapter(MainActivity.this, (ArrayList<Venue>) venueArrayList);
                                listview.setAdapter(nearAdapter);


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);

            }
        });


    }
}




