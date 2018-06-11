package com.cola.sliderviewdemo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cola.sliderviewdemo.view.SliderView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        final SliderView sliderView = findViewById(R.id.slider_view);
        final Button leftMoveBtn = (Button)findViewById(R.id.left_move_btn);
        leftMoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sliderView.moveToDestinationView(leftMoveBtn);
            }
        });
        final Button rightMoveBtn = (Button)findViewById(R.id.right_move_btn);
        rightMoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sliderView.moveToDestinationView(rightMoveBtn);
            }
        });
    }
}
