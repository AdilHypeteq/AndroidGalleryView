package com.hypeteq.galleryview;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class GalleryView {

    public static ActionCallback actionCallback;

    public static void show(Context context, ArrayList<String> paths, int position){
        Intent intent = new Intent(context, GalleryViewActivity.class);
        intent.putExtra("items", paths);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    public static void show(Context context, ArrayList<String> paths, int position, ActionCallback callback){
        actionCallback = callback;
        Intent intent = new Intent(context, GalleryViewActivity.class);
        intent.putExtra("items", paths);
        intent.putExtra("position", position);
        intent.putExtra("callback", 1);
        context.startActivity(intent);

    }

    public static void show(Context context, ArrayList<String> paths){
        GalleryView.show(context,paths,0);
    }
}
