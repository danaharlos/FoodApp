package com.example.foodapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class decisionScreen extends AppCompatActivity {
    boolean openNow; //true if user only wants to show restaurants that are open at that time
    private RadioGroup radiusRadioGroup; //picks the radius

    /**
     * On creation of decision screen activity, sets up switches and radio group.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision_screen);
        Switch openNowSwitch = (Switch) findViewById(R.id.switch1);

        //Set a CheckedChange Listener for open now switch
        openNowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                openNow = on;
            }
        });

        radiusRadioGroup = (RadioGroup) findViewById(R.id.radiusGroup);
}

    /**
     * Updates the openNow boolean based off of whether or not the user wants to display only restaurants
     * that are open at that time.
     * @param v
     */
    public void openSwitchClicked(View v){
        //Is the switch on?
        boolean on = ((Switch) v).isChecked();
        openNow = on;
    }

    /**
     * This method called when the Start Search button is pressed, it collects the parameters the user inputted,
     * stops the decisionScreen activity and starts the MapActivity which shows the map with the nearby restaurants
     * based off the users parameters
     * @param v
     */
    public void startSearch(View v){
        EditText simpleEditText = (EditText) findViewById(R.id.editText); //get keyword from text box
        String keyword = simpleEditText.getText().toString();
        if(keyword.equals("")){
            keyword = "null";
        }
        Log.d("searchText", "The search keyword is:"+keyword);
        int radius = updateRadius(v);

        Intent intent = new Intent(
                decisionScreen.this, MapActivity.class);
        intent.putExtra("openNow", openNow);
        intent.putExtra("radius", radius);
        intent.putExtra("keyword", keyword);
        finish(); //stops history for decision screen activity class
        startActivity(intent);

    }

    /**
     * Returns the radius (in meters) that the user wants their restaurant search to include
     * @param v, current view
     * @return radius (in meters)
     */
    public int updateRadius(View v) {
        RadioButton rb = (RadioButton) radiusRadioGroup.findViewById(radiusRadioGroup.getCheckedRadioButtonId());
        if(rb.getText().toString().equals("2 km")) {
            Log.d("radius", "2km pressed");
            return 2000;
        }
        if(rb.getText().toString().equals("1 km")) {
            Log.d("radius", "1km pressed");
            return 1000;
        }
        else{
            Log.d("radius", "0.5km pressed");
            Log.d("radius", "rb message is: "+rb.getText());
            Log.d("radius", "rb message is:\n"+rb.getText()+"\n"+"3 km");
            return 500; //if there is error and no button is pushed, default to 0.5 km
        }
    }
}
