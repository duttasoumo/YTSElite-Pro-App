package com.dcpsoft.ytselitepro;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dcpsoft.ytselitepro.Networking.AsyncCallback;
import com.dcpsoft.ytselitepro.Networking.HttpGetAsync;
import com.dcpsoft.ytselitepro.View.CastAdapter;
import com.dcpsoft.ytselitepro.View.CastData;
import com.dcpsoft.ytselitepro.View.ExpandableHeightListView;
import com.dcpsoft.ytselitepro.View.TextAwesome;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Soumya on 23-Jul-16.
 */
public class MovieDetails extends AppCompatActivity {

    private ImageView mainImg;
    private TextView movieNameTxt,rateTxt,genreTxt,yearTxt,synopsisTxt;
    private LinearLayout screenShotHori,lin720p,lin1080p,lin3d;
    private FancyButton but720p,but1080p,but3d;
    private TextView size720p,size1080p,size3d;
    private TextView runtimeTxt;
    private String movie_id;
    private FancyButton youtubeBut,subtitleBut;
    private ProgressBar castProgress;
    private ExpandableHeightListView castList;

    private String moview_imdb_code;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_movie_details);

        movie_id = getIntent().getExtras().getString("movie_id");
        mainImg=(ImageView)findViewById(R.id.movie_image);
        movieNameTxt=(TextView)findViewById(R.id.movie_name_txt);
        rateTxt=(TextView)findViewById(R.id.rating_txt);
        genreTxt=(TextView)findViewById(R.id.genre_txt);
        yearTxt=(TextView)findViewById(R.id.year_txt);
        synopsisTxt=(TextView)findViewById(R.id.synopsis_content_text);
        screenShotHori = (LinearLayout)findViewById(R.id.hori_image_lin);

        lin720p=(LinearLayout)findViewById(R.id.lin_720p);
        lin1080p=(LinearLayout)findViewById(R.id.lin_1080p);
        lin3d=(LinearLayout)findViewById(R.id.lin_3d);

        but720p = (FancyButton)findViewById(R.id.but_720p);
        but1080p = (FancyButton)findViewById(R.id.but_1080p);
        but3d = (FancyButton)findViewById(R.id.but_3d);

        size720p=(TextView)findViewById(R.id.txt_720p_size);
        size1080p=(TextView)findViewById(R.id.txt_1080p_size);
        size3d=(TextView)findViewById(R.id.txt_3d_size);
        runtimeTxt=(TextView)findViewById(R.id.runtime_txt);

        youtubeBut=(FancyButton)findViewById(R.id.youtube_trailer_but);
        subtitleBut=(FancyButton)findViewById(R.id.subtitles_but);

        castProgress = (ProgressBar)findViewById(R.id.cast_loader);
        castList = (ExpandableHeightListView)findViewById(R.id.cast_list);
        castList.setVisibility(View.GONE);





        new HttpGetAsync("https://yts.ag/api/v2/movie_details.json?movie_id=" + movie_id + "&with_images=true&with_cast=true", new AsyncCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish(String jsonReceive) {
                try {
                    final JSONObject jsonObject = (new JSONObject(jsonReceive).optJSONObject("data")).optJSONObject("movie");
                    if(jsonObject!=null)
                    {
                        movieNameTxt.setText(jsonObject.getString("title"));
                        rateTxt.setText(" "+jsonObject.getString("rating") + " / 10");
                        yearTxt.setText(jsonObject.getString("year"));

                        int runtime_int=jsonObject.getInt("runtime");
                        runtimeTxt.setText(getTimeString(runtime_int));

                        Picasso.with(MovieDetails.this).load(jsonObject.getString("medium_cover_image")).placeholder(R.drawable.yts_loading_img).into(mainImg);

                        synopsisTxt.setText(jsonObject.getString("description_full"));

                        JSONArray genreArray = jsonObject.getJSONArray("genres");

                        String genreT = genreArray.getString(0);
                        for(int i=1;i<genreArray.length();i++)
                            genreT += "  "+genreArray.getString(i);

                        genreTxt.setText(genreT);

                        moview_imdb_code = jsonObject.getString("imdb_code");
                        youtubeBut.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Intent intent = YouTubeStandalonePlayer.createVideoIntent(MovieDetails.this,getResources().getString(R.string.youtube_api_key),jsonObject.getString("yt_trailer_code"));
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        subtitleBut.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.yifysubtitles.com/movie-imdb/"+jsonObject.getString("imdb_code"))));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        final ArrayList<String> img_screen_pics=new ArrayList<String>();
                        if(jsonObject.optString("medium_screenshot_image1").length()>0)
                        {
                            ImageView img1=new ImageView(MovieDetails.this);
                            LinearLayout.LayoutParams lay=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            lay.setMargins(10,0,10,0);
                            img1.setLayoutParams(lay);
                            img1.setAdjustViewBounds(true);
                            screenShotHori.addView(img1);
                            Picasso.with(MovieDetails.this).load(jsonObject.getString("medium_screenshot_image1")).into(img1);

                            img_screen_pics.add(jsonObject.getString("medium_screenshot_image1"));
                        }
                        if(jsonObject.optString("medium_screenshot_image2").length()>0)
                        {
                            ImageView img1=new ImageView(MovieDetails.this);
                            LinearLayout.LayoutParams lay=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            lay.setMargins(10,0,10,0);
                            img1.setLayoutParams(lay);
                            img1.setAdjustViewBounds(true);
                            screenShotHori.addView(img1);
                            Picasso.with(MovieDetails.this).load(jsonObject.getString("medium_screenshot_image2")).into(img1);


                            img_screen_pics.add(jsonObject.getString("medium_screenshot_image2"));
                        }
                        if(jsonObject.optString("medium_screenshot_image3").length()>0)
                        {
                            ImageView img1=new ImageView(MovieDetails.this);
                            LinearLayout.LayoutParams lay=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            lay.setMargins(10,0,10,0);
                            img1.setLayoutParams(lay);
                            img1.setAdjustViewBounds(true);

                            screenShotHori.addView(img1);
                            Picasso.with(MovieDetails.this).load(jsonObject.getString("medium_screenshot_image3")).into(img1);


                            img_screen_pics.add(jsonObject.getString("medium_screenshot_image3"));

                        }

                        if(img_screen_pics.size()>0)
                        {
                            screenShotHori.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent i=new Intent(MovieDetails.this,ScreenshotView.class);
                                    Bundle b = new Bundle();
                                    b.putStringArrayList("img_urls",img_screen_pics);
                                    i.putExtras(b);
                                    startActivity(i);
                                }
                            });
                        }

                        JSONArray castArray = jsonObject.getJSONArray("cast");
                        List<CastData> castDataList = new ArrayList<CastData>();
                        for(int i=0;i<castArray.length();i++){
                            JSONObject jsonObject1 = castArray.getJSONObject(i);
                            String tmpImg= jsonObject1.optString("url_small_image");
                            tmpImg = (tmpImg.length()>0)? tmpImg : "noPic";
                            castDataList.add(new CastData(jsonObject1.getString("name"),"as "+jsonObject1.getString("character_name"),tmpImg));
                        }
                        loadCast(castDataList);
                        JSONArray torrentsArray = jsonObject.getJSONArray("torrents");

                        lin720p.setVisibility(ViewGroup.GONE);
                        lin1080p.setVisibility(ViewGroup.GONE);
                        lin3d.setVisibility(ViewGroup.GONE);

                        for(int i=0;i<torrentsArray.length();i++)
                        {
                            final JSONObject torJsonObject= torrentsArray.getJSONObject(i);

                            if(torJsonObject.getString("quality").equalsIgnoreCase("3D"))
                            {
                                lin3d.setVisibility(View.VISIBLE);
                                size3d.setText(torJsonObject.getString("size"));
                                but3d.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(torJsonObject.getString("url"))));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.e("torrent click error",e.getMessage());
                                        }
                                    }
                                });
                            }
                            else if(torJsonObject.getString("quality").equalsIgnoreCase("720p"))
                            {
                                lin720p.setVisibility(View.VISIBLE);
                                size720p.setText(torJsonObject.getString("size"));
                                but720p.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(torJsonObject.getString("url"))));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.e("torrent click error",e.getMessage());
                                        }
                                    }
                                });
                            }
                            else if(torJsonObject.getString("quality").equalsIgnoreCase("1080p"))
                            {
                                lin1080p.setVisibility(View.VISIBLE);
                                size1080p.setText(torJsonObject.getString("size"));
                                but1080p.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(torJsonObject.getString("url"))));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.e("torrent click error",e.getMessage());
                                        }
                                    }
                                });
                            }
                        }


                    }
                    else{
                        Intent i=new Intent(MovieDetails.this,OfflineActivity.class);
                        Bundle b=new Bundle();
                        b.putString("movie_id",movie_id);
                        i.putExtras(b);
                        startActivity(i);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("json error",e.getMessage());
                }
            }
        }).execute();

    }
    private String getTimeString(int time)
    {
        String result=" ";
        if(time/60 <1)
        {
            return time%60+" minutes";
        }
        else{

            if(time/60 == 1) result+=" 1 hour ";
            else result+= time/60+" hours ";

            result+= time%60 +" minutes";
        }
        return result;
    }

    private void loadCast(final List<CastData> incomingCast)
    {
        new HttpGetAsync("http://www.omdbapi.com/?i=" + moview_imdb_code, new AsyncCallback() {
            @Override
            public void onStart() {
                castProgress.setVisibility(View.VISIBLE);
                castProgress.setIndeterminate(true);
                castList.setVisibility(View.GONE);
            }

            @Override
            public void onFinish(String jsonReceive) {
                castProgress.setVisibility(View.GONE);
                List<CastData> castDataList=new ArrayList<CastData>();
                if(jsonReceive!=null)
                {
                    try{
                        JSONObject jsonObject=new JSONObject(jsonReceive);
                        if(jsonObject.getString("Director").length()>5)
                            castDataList.add(new CastData(jsonObject.getString("Director"),"Director","noPic"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("omdb json error",e.getMessage());
                    }

                }
                castDataList.addAll(incomingCast);
                CastAdapter castAdapter=new CastAdapter(MovieDetails.this,castDataList);
                castList.setVisibility(View.VISIBLE);
                castList.setAdapter(castAdapter);
            }
        }).execute();
    }


}
