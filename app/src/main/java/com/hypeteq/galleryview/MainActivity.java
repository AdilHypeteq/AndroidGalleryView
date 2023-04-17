package com.hypeteq.galleryview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> paths = new ArrayList<>();
        paths.add("Path1");
        paths.add("Path2");
        paths.add("Path3");
        GalleryView.show(this, paths, 0);
    }
}