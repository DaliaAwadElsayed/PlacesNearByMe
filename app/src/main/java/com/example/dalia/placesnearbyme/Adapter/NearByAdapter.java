package com.example.dalia.placesnearbyme.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dalia.placesnearbyme.DataModel.Venue;
import com.example.dalia.placesnearbyme.Location.GPSTracker;
import com.example.dalia.placesnearbyme.MainActivity;
import com.example.dalia.placesnearbyme.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NearByAdapter extends ArrayAdapter<Venue> {
    String latitude,longitude;

    public NearByAdapter(Context context, ArrayList<Venue> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Venue venue = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
        }

        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.textViewnamerowid);
        TextView address = (TextView) convertView.findViewById(R.id.textViewaddressid);
        ImageView img = (ImageView) convertView.findViewById(R.id.imagerowid);
        ImageView tele = (ImageView) convertView.findViewById(R.id.imgteleid);
        ImageView maps = (ImageView) convertView.findViewById(R.id.imggoogleid);

        // Populate the data into the template view using the data object
        name.setText(venue.getName());
        if (venue.getLocation() != null) {
            address.setText(venue.getLocation().getAddress());
        } else {
            address.setText("Address not Found!");
        }
        if (venue.getCategories() != null && venue.getCategories().size() != 0 && venue.getCategories().get(0).getIcon() != null) {
            Picasso.with(getContext()).load(venue.getCategories().get(0).getIcon().getPrefix() + "bg_64" + venue.getCategories().get(0).getIcon().getSuffix()).into(img);

        }
        tele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String x = venue.getContact().getPhone();
                if (x == null) {
                    Toast.makeText(getContext(), "No phone number", Toast.LENGTH_SHORT).show();
                } else {
                    Uri number = Uri.parse("tel:" + x);
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                    getContext().startActivity(callIntent);
                }
            }
        });
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GPSTracker gps = new GPSTracker(getContext());
                if (gps.canGetLocation()) {
                    latitude = String.valueOf(gps.getLatitude());
                    longitude = String.valueOf(gps.getLongitude());
                }
                String label = venue.getName();
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                getContext().startActivity(intent);
            }
        });
        // Return the completed view to render on screen
        return convertView;


    }
}