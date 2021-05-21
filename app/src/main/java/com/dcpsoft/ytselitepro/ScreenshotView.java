package com.dcpsoft.ytselitepro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dcpsoft.ytselitepro.View.DepthPageTransformer;
import com.dcpsoft.ytselitepro.View.ScreenshotAdapter;

/**
 * Created by Soumya on 30-Jul-16.
 */
public class ScreenshotView extends AppCompatActivity {

    private ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshots);

        Log.e("screen pics",getIntent().getExtras().getStringArrayList("img_urls").toString());
        viewPager=(ViewPager)findViewById(R.id.screenshot_viewpager);
        viewPager.setAdapter(new ScreenshotAdapter(ScreenshotView.this,getIntent().getExtras().getStringArrayList("img_urls")));
        viewPager.setPageTransformer(true, new DepthPageTransformer());

    }
}
