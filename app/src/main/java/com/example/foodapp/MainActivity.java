package com.example.foodapp;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {
    private GestureDetectorCompat gestureObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestureObject = new GestureDetectorCompat(this, new LearnGesture());
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


            } else if (event2.getX() < event1.getX()) {
                //this is right to left swipe
                Intent intent = new Intent(
                        MainActivity.this, decisionScreen.class);
                finish(); //stops history for main activity class
                startActivity(intent);

            }
            return true;
        }


    }
}

