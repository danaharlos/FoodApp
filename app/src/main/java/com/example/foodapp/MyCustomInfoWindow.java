package com.example.foodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MyCustomInfoWindow implements GoogleMap.InfoWindowAdapter{
    private Context myContext;
    private final View myView;

    public MyCustomInfoWindow(Context context){
        myContext=context;
        myView = LayoutInflater.from(context).inflate(R.layout.custom_window, null);
    }
    @Override
    public View getInfoWindow(Marker marker) {

        TextView title = (TextView)myView.findViewById(R.id.location);
        //TextView rateText = (TextView)myView.findViewById(R.id.rateText);
        TextView snippet = (TextView)myView.findViewById(R.id.snippet);

        LatLng loc = marker.getPosition();
        title.setText(marker.getTitle());
        //rateText.setText();
        snippet.setText(marker.getSnippet());

        return myView;
    }

    @Override
    public View getInfoContents(Marker marker) {

        TextView location = (TextView)myView.findViewById(R.id.location);
        //TextView rateText = (TextView)myView.findViewById(R.id.rateText);
        TextView snippet = (TextView)myView.findViewById(R.id.snippet);

        LatLng loc = marker.getPosition();
        location.setText(marker.getTitle());
        //rateText.setText(String.valueOf(loc.latitude));
        snippet.setText(marker.getSnippet());

        return myView;

    }
}
