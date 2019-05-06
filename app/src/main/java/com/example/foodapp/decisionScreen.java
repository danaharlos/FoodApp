package com.example.foodapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class decisionScreen extends AppCompatActivity {
    boolean openNow; //true if user only wants to show restaurants that are open at that time
    private RadioGroup radiusRadioGroup;
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

    public void startSearch(View v){

        int radius = updateRadius(v);

        Intent intent = new Intent(
                decisionScreen.this, MapActivity.class);
        intent.putExtra("openNow", openNow);
        intent.putExtra("radius", radius);
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
        if(rb.getText() == "10 km") {
            return 10000;
        }
        if(rb.getText() == "3 km") {
            return 3000;
        }
        else return 1000; //if there is error and no button is pushed, default to 1km
    }
}
