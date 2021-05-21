package com.dcpsoft.ytselitepro.View;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dcpsoft.ytselitepro.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Soumya on 30-Jul-16.
 */
public class ScreenshotAdapter extends PagerAdapter {
    ArrayList<String> img_urls;
    LayoutInflater inflater;
    Context mContext;
    public ScreenshotAdapter(Context mContext, ArrayList<String> img_urls) {
        this.img_urls = img_urls;
        this.mContext=mContext;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = inflater.inflate(R.layout.screenshot_image,container,false);
        final ImageView imageView = (ImageView)itemView.findViewById(R.id.screen_img);
        Picasso.with(mContext).load(img_urls.get(position)).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

               PhotoViewAttacher mAttacher=new PhotoViewAttacher(imageView);
            }

            @Override
            public void onError() {

            }
        });
        container.addView(itemView);
        return itemView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
