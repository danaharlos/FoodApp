package com.example.foodapp;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private GestureDetectorCompat gestureObject;
    private TextView maintext, swipetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        maintext = (TextView)findViewById(R.id.title);
        swipetext = (TextView)findViewById(R.id.swipe);
        maintext.bringToFront();
        swipetext.bringToFront();


    }

    @Override //taken from https://www.youtube.com/watch?v=Q5Ndr944U2o
    public boolean onTouchEvent(MotionEvent event){
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    class LearnGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY){
            if(event2.getX() > event1.getX()) {
                //this is left to right swipe
                //do nothing!
            } else if (event2.getX() < event1.getX()) {
                //this is right to left swipe
                //go to decision screen
                Intent intent = new Intent(
                        MainActivity.this, decisionScreen.class);
                finish(); //stops history for main activity class
                startActivity(intent);

            }
            return true;
        }


    }
}

