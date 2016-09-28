package com.zhl.photopicker.demo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhl.photopicker.Picker;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picker.create().max(10).showGif().start(MainActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == Picker.REQUEST_PICKER) {
            ArrayList<String> pathList = Picker.getOutput(data);

            if (pathList != null) {
                for (String path : pathList) {
                    System.out.println(path);
                }
            } else {
                System.out.println(Picker.getSingleOutput(data));
            }
        }
    }
}
