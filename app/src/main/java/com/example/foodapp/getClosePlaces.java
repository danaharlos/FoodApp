package com.example.foodapp;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class getClosePlaces extends AsyncTask<Object, String, String> {

    GoogleMap nMap;
    InputStream inputstream;
    String url;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String rawData;
//https://www.youtube.com/watch?v=YlJk-YCmomg&list=PLF0BIlN2vd8und4ajF-bdFI3jWyPTXxB5&index=2 tutorial
    @Override
    protected String doInBackground(Object... objects) {
        nMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        try {
            URL thisurl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)thisurl.openConnection();
            httpURLConnection.connect();
            inputstream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputstream));
            String x = "";
            stringBuilder = new StringBuilder();
            while((x =bufferedReader.readLine())!=null){
                stringBuilder.append(x);
            }
            rawData = stringBuilder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rawData;
    }

    /**
     * Fetches and parses json from google places API and displays
     * them to google maps
     * @param s json response from google places web api
     */
    @Override
    protected void onPostExecute(String s) {
        //Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

        try {
            JSONObject parentObject = new JSONObject(s);
            JSONArray resultsArray = parentObject.getJSONArray("results");

            for(int i = 0; i<resultsArray.length(); i++){
                JSONObject jsonObject = resultsArray.getJSONObject(i);
                JSONObject locationObject = jsonObject.getJSONObject("geometry").getJSONObject("location");
                String latitude = locationObject.getString("lat");
                String longitude = locationObject.getString("lng");
                JSONObject nameObject = resultsArray.getJSONObject((i));
                String name_restaurant = nameObject.getString("name");
                String vicinity = nameObject.getString("vicinity");

                LatLng latLng= new LatLng(Double.parseDouble((latitude)), Double.parseDouble(longitude));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(name_restaurant);
                markerOptions.position(latLng);

                nMap.addMarker(markerOptions);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
