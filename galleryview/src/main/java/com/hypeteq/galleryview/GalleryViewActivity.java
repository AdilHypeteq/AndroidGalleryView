package com.hypeteq.galleryview;

import static com.hypeteq.galleryview.GalleryView.actionCallback;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class GalleryViewActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST = 111;

    ArrayList<String> filelist = new ArrayList<>();
    int position = 0;
    private GalleryPagerAdapter _adapter;
    ViewPager _pager;
    ImageView _closeButton;
    RelativeLayout rlBack;
    TextView lblSize;

    int callback;
    public String TAG = GalleryViewActivity.this.getClass().getSimpleName();

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gallery_view);
        Bundle b = getIntent().getExtras();
        _pager = (ViewPager) findViewById(R.id.pager);
        _closeButton = (ImageView) findViewById(R.id.btn_close);
        lblSize = findViewById(R.id.txtSize);
        rlBack = findViewById(R.id.rlback);

        position = b.getInt("position", 0);
        callback = b.getInt("callback", 0);
        filelist = b.getStringArrayList("items");

        _closeButton.setOnClickListener(v -> {
            Log.d("Close", "Close clicked");
            finish();
        });

        rlBack.setOnClickListener(view -> finish());

        final ImageView _btn_action = (ImageView) findViewById(R.id.btn_action);
        if (callback == 1) {
            _btn_action.setOnClickListener(v -> {
                actionCallback.onAction(filelist.get(position), position);
                filelist.remove(position);
                _adapter.notifyDataSetChanged();
                if (filelist.size() == 0) {
                    _btn_action.setVisibility(View.GONE);
                }
            });
        } else {
            _btn_action.setVisibility(View.GONE);
        }
        if (filelist.size() == 0) {
            _btn_action.setVisibility(View.GONE);
        }

        if (checkWriteExternalPermission())
            _init();
        else
            grantPermission();


    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    private boolean checkWriteExternalPermission() {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void grantPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    _init();
                }
            }
        }
    }


    public void _init() {
        _adapter = new GalleryPagerAdapter(this);
        _pager.setAdapter(_adapter);
        _pager.setOffscreenPageLimit(1); // how many images to load into memory_pager
        _pager.setCurrentItem(position);

        int current_pos = position + 1;

        lblSize.setText("" + current_pos + " of " + filelist.size());

        _pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pos) {
                position = pos;
                int current_pos = position + 1;

                lblSize.setText("" + current_pos + " of " + filelist.size());
                JZVideoPlayer.releaseAllVideos();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    class GalleryPagerAdapter extends PagerAdapter {

        final Context _context;
        final LayoutInflater _inflater;

        boolean isFullscreen = false;

        public GalleryPagerAdapter(Context context) {
            _context = context;
            _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return filelist.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            String path = filelist.get(position);
            String extension = path.substring(path.lastIndexOf("."));

            Log.e("Extension", extension);

            View itemView = null;
            if (extension.equalsIgnoreCase(".mp4") || extension.equalsIgnoreCase(".avi") || extension.equalsIgnoreCase(".mkv")
                    || extension.contains("3gp") || extension.contains("mov") || extension.contains("webm")) {
                itemView = _inflater.inflate(R.layout.pager_video_item, container, false);
                container.addView(itemView);

                final JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) itemView.findViewById(R.id.videoplayer);
                jzVideoPlayerStandard.setUp(path,
                        JZVideoPlayerStandard.SCREEN_WINDOW_LIST,
                        "");
                Glide.with(_context).load(path)
                        .into(jzVideoPlayerStandard.thumbImageView);

            } else {
                itemView = _inflater.inflate(R.layout.pager_gallery_item, container, false);
                container.addView(itemView);
                final SubsamplingScaleImageView imageView =
                        (SubsamplingScaleImageView) itemView.findViewById(R.id.image);
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(filelist.get(position))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap image,
                                                        Transition<? super Bitmap> transition) {

                                int xDim = image.getWidth();
                                int yDim = image.getHeight();
                                if (xDim <= 4096 && yDim <= 4096) {
                                    imageView.setImage(ImageSource.bitmap(image));
                                } else {
                                    if (xDim > yDim) {
                                        int nh = (int) (image.getHeight() * (4096f / image.getWidth()));
                                        Bitmap scaled = Bitmap.createScaledBitmap(image, 4096, nh, true);
                                        imageView.setImage(ImageSource.bitmap(scaled));
                                    } else {
                                        int nh = (int) (image.getWidth() * (4096f / image.getHeight()));
                                        Bitmap scaled = Bitmap.createScaledBitmap(image, nh, 4096, true);
                                        imageView.setImage(ImageSource.bitmap(scaled));
                                    }
                                }

                            }
                        });
            }


            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
