package com.ajinkyabadve.customviewpractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ajinkyabadve.circularratingloader.CircularRating;

public class MainActivity extends AppCompatActivity {
    CircularRating circularRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circularRating = (CircularRating) findViewById(R.id.view);
        circularRating.animateArc(1000);
    }
}
