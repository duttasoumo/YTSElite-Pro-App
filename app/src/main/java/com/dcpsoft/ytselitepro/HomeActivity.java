package com.dcpsoft.ytselitepro;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dcpsoft.ytselitepro.Networking.AsyncCallback;
import com.dcpsoft.ytselitepro.Networking.HttpGetAsync;
import com.dcpsoft.ytselitepro.View.EndlessScrollListener;
import com.dcpsoft.ytselitepro.View.HomeMovieAdapter;
import com.dcpsoft.ytselitepro.View.HomeMovieData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private GridView gridView;
    private HomeMovieAdapter homeMovieAdapter;
    private CoordinatorLayout coordinatorLayout;
    private  Snackbar snackbar;
    private LinearLayout filterLin,oopsLin;
    private Spinner qualitySpin,genreSpin,ratingSpin,orderbySpin;
    private FancyButton searchBtn;
    private EditText movieNameEdit;
    private FloatingActionButton fabButton;
    private ProgressBar progressBarHori;

    private int filterHeight=0;
    private int pageLimit=0;

    private final String[] quality_strings={"All","720p","1080p","3D"};
    private final String[] genre_strings=    {
            "All",
            "Action" ,
            "Adventure" ,
            "Animation" ,
            "Biography" ,
            "Comedy" ,
            "Crime" ,
            "Documentary" ,
            "Drama" ,
            "Family" ,
            "Fantasy" ,
            "Film-Noir" ,
            "Game-Show" ,
            "History" ,
            "Horror" ,
            "Music" ,
            "Musical" ,
            "Mystery" ,
            "News" ,
            "Reality-TV" ,
            "Romance" ,
            "Sci-Fi" ,
            "Sport" ,
            "Talk-Show" ,
            "Thriller" ,
            "War" ,
            "Western"};
    private final String[] rating_string_spin ={"All","9+","8+","7+","6+","5+","4+","3+","2+","1+"};
    private final String[] rating_string ={"All","9","8","7","6","5","4","3","2","1"};
    private final String[] order_by_string={"Latest","Oldest","Seeds","Peers","Year","Rating","Likes","Alphabetical","Downloads"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.home_coordinator);
        gridView=(GridView)findViewById(R.id.home_grid);
        filterLin=(LinearLayout)findViewById(R.id.filter_lin);
        oopsLin=(LinearLayout)findViewById(R.id.oops_lin);
        fabButton=(FloatingActionButton)findViewById(R.id.fab);
        movieNameEdit=(EditText)findViewById(R.id.movie_name_edit);
        searchBtn=(FancyButton)findViewById(R.id.search_btn);
        progressBarHori=(ProgressBar)findViewById(R.id.div_progress);

        qualitySpin = (Spinner)findViewById(R.id.quality_spin);
        genreSpin = (Spinner)findViewById(R.id.genre_spin);
        ratingSpin = (Spinner)findViewById(R.id.rating_spin);
        orderbySpin = (Spinner)findViewById(R.id.order_by_spin);

        oopsLin.setVisibility(View.GONE);
        gridView.setVisibility(View.GONE);





        ArrayAdapter<String> adapt1= new ArrayAdapter<String>(HomeActivity.this,R.layout.simple_spinner_item,quality_strings);
        adapt1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        ArrayAdapter<String> adapt2= new ArrayAdapter<String>(HomeActivity.this,R.layout.simple_spinner_item,genre_strings);
        adapt2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        ArrayAdapter<String> adapt3= new ArrayAdapter<String>(HomeActivity.this,R.layout.simple_spinner_item, rating_string_spin);
        adapt3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        ArrayAdapter<String> adapt4= new ArrayAdapter<String>(HomeActivity.this,R.layout.simple_spinner_item,order_by_string);
        adapt4.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        qualitySpin.setAdapter(adapt1);
        genreSpin.setAdapter(adapt2);
        ratingSpin.setAdapter(adapt3);
        orderbySpin.setAdapter(adapt4);


        ViewTreeObserver vto=filterLin.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(filterLin.getMeasuredHeight()>filterHeight)
                    filterHeight=filterLin.getMeasuredHeight();

            }
        });
        //filterLin.setVisibility(View.INVISIBLE);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterToggle();
            }
        });
        fabButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                fabButton.performClick();
            }
        },500);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri.Builder callingUri=Uri.parse("https://yts.ag/api/v2/list_movies.json").buildUpon();

                if(movieNameEdit.getText().toString().trim().length()>0)
                    callingUri.appendQueryParameter("query_term",movieNameEdit.getText().toString().trim());

                callingUri.appendQueryParameter("quality",qualitySpin.getSelectedItem().toString())
                        .appendQueryParameter("minimum_rating", rating_string[ratingSpin.getSelectedItemPosition()]);
                if(genreSpin.getSelectedItemPosition()!=0)
                    callingUri.appendQueryParameter("genre",genreSpin.getSelectedItem().toString());

                //order by cases
                if(order_by_string[orderbySpin.getSelectedItemPosition()].equalsIgnoreCase(order_by_string[0]))
                {
                    //Latest
                    callingUri.appendQueryParameter("sort_by","date_added")
                            .appendQueryParameter("order_by","desc");
                }
                else if(order_by_string[orderbySpin.getSelectedItemPosition()].equalsIgnoreCase(order_by_string[1]))
                {

                    //Oldest
                    callingUri.appendQueryParameter("sort_by","date_added")
                            .appendQueryParameter("order_by","asc");
                }
                else if(order_by_string[orderbySpin.getSelectedItemPosition()].equalsIgnoreCase(order_by_string[2]))
                {

                    //seeds
                    callingUri.appendQueryParameter("sort_by","seeds")
                            .appendQueryParameter("order_by","desc");
                }
                else if(order_by_string[orderbySpin.getSelectedItemPosition()].equalsIgnoreCase(order_by_string[3]))
                {

                    //peers
                    callingUri.appendQueryParameter("sort_by","peers")
                            .appendQueryParameter("order_by","desc");
                }
                else if(order_by_string[orderbySpin.getSelectedItemPosition()].equalsIgnoreCase(order_by_string[4]))
                {

                    //year
                    callingUri.appendQueryParameter("sort_by","year")
                            .appendQueryParameter("order_by","desc");
                }
                else if(order_by_string[orderbySpin.getSelectedItemPosition()].equalsIgnoreCase(order_by_string[5]))
                {

                    //rating
                    callingUri.appendQueryParameter("sort_by","rating")
                            .appendQueryParameter("order_by","desc");
                }
                else if(order_by_string[orderbySpin.getSelectedItemPosition()].equalsIgnoreCase(order_by_string[6]))
                {

                    //Likes
                    callingUri.appendQueryParameter("sort_by","like_count")
                            .appendQueryParameter("order_by","desc");
                }
                else if(order_by_string[orderbySpin.getSelectedItemPosition()].equalsIgnoreCase(order_by_string[7]))
                {

                    //alphabetical
                    callingUri.appendQueryParameter("sort_by","title")
                            .appendQueryParameter("order_by","asc");
                }
                else if(order_by_string[orderbySpin.getSelectedItemPosition()].equalsIgnoreCase(order_by_string[8]))
                {

                    //downloads
                    callingUri.appendQueryParameter("sort_by","download_count")
                            .appendQueryParameter("order_by","desc");
                }


                String tmp=callingUri.build().toString();
                Log.e("query",tmp);
                loadMovies(tmp);


            }
        });

        movieNameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionInfo, KeyEvent keyEvent) {
                if(actionInfo   == EditorInfo.IME_ACTION_SEARCH){
                    searchBtn.performClick();
                    hideKeyboard(HomeActivity.this);
                    return true;
                }
                return false;
            }
        });

        loadMovies("https://yts.ag/api/v2/list_movies.json?limit=10");






    }
    private void loadMovies(String url)
    {
        new HttpGetAsync(url, new AsyncCallback() {
            @Override
            public void onStart() {
                Log.e("yts","fetch started");
                progressBarHori.setIndeterminate(true);
            }

            @Override
            public void onFinish(String jsonReceive) {

                progressBarHori.setIndeterminate(false);
                try{
                    if(jsonReceive!=null) {
                        JSONObject jData = new JSONObject(jsonReceive).getJSONObject("data");

                        pageLimit = (int) Math.ceil(jData.getInt("movie_count") * 1.0 / jData.getInt("limit"));

                        if (pageLimit > 0) {

                            oopsLin.setVisibility(View.GONE);
                            gridView.setVisibility(View.VISIBLE);

                            JSONArray jsonArray = jData.getJSONArray("movies");
                            List<HomeMovieData> movie_data = new ArrayList<HomeMovieData>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                HomeMovieData homeMovieData = new HomeMovieData();

                                homeMovieData.setMovieImgUrl(jsonObject.getString("medium_cover_image"));
                                homeMovieData.setMovieName(jsonObject.getString("title_english"));
                                homeMovieData.setMovieID(jsonObject.getString("id"));
                                homeMovieData.setMovieYear(jsonObject.getString("year"));
                                homeMovieData.setMovieRate(jsonObject.getString("rating"));

                                movie_data.add(homeMovieData);
                            }

                            homeMovieAdapter = new HomeMovieAdapter(HomeActivity.this, movie_data);
                            gridView.setAdapter(homeMovieAdapter);

                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    Intent i = new Intent(HomeActivity.this, MovieDetails.class);
                                    Bundle b = new Bundle();
                                    b.putString("movie_id", homeMovieAdapter.getItem(position).getMovieID());
                                    i.putExtras(b);
                                    startActivity(i);
                                }
                            });
                            gridView.setOnScrollListener(new EndlessScrollListener() {
                                @Override
                                public boolean onLoadMore(int page, int totalItemsCount) {

                                    if (snackbar != null) snackbar.dismiss();
                                    if (page <= pageLimit) {
                                        snackbar = Snackbar.make(coordinatorLayout, "Loading page " + page + " of " + pageLimit, Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                        customLoadMoreDataFromApi(page);
                                    } else {

                                        snackbar = Snackbar.make(coordinatorLayout, "Sorry no more movies available", Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                    }


                                    return true;
                                }
                            });
                        } else {

                            oopsLin.setVisibility(View.VISIBLE);
                            gridView.setVisibility(View.GONE);

                        }
                    }
                    else
                    {
                        startActivity(new Intent(HomeActivity.this,OfflineActivity.class));
                        HomeActivity.this.finish();
                    }
                } catch (JSONException e) {
                    Log.e("json error",e.getMessage());
                }
            }
        }).execute();
    }

    private void customLoadMoreDataFromApi(final int page)
    {
        new HttpGetAsync("https://yts.ag/api/v2/list_movies.json?limit=10&page="+page, new AsyncCallback() {
            @Override
            public void onStart() {
                Log.e("yts","load page"+page);
                progressBarHori.setIndeterminate(true);
            }

            @Override
            public void onFinish(String jsonReceive) {
                try {
                    progressBarHori.setIndeterminate(false);

                    if(jsonReceive!=null) {
                        JSONArray jsonArray = new JSONObject(jsonReceive).getJSONObject("data").getJSONArray("movies");
                        List<HomeMovieData> movie_data = new ArrayList<HomeMovieData>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            HomeMovieData homeMovieData = new HomeMovieData();

                            homeMovieData.setMovieImgUrl(jsonObject.getString("medium_cover_image"));
                            homeMovieData.setMovieName(jsonObject.getString("title_english"));
                            homeMovieData.setMovieID(jsonObject.getString("id"));
                            homeMovieData.setMovieYear(jsonObject.getString("year"));
                            homeMovieData.setMovieRate(jsonObject.getString("rating"));

                            movie_data.add(homeMovieData);
                        }

                        homeMovieAdapter.addAll(movie_data);
                        homeMovieAdapter.notifyDataSetChanged();
                    }
                    else{
                        startActivity(new Intent(HomeActivity.this,OfflineActivity.class));
                        HomeActivity.this.finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        ).execute();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void filterToggle(){



        if(filterLin.getVisibility() == View.VISIBLE)
        {
            hideKeyboard(HomeActivity.this);
            final int height= filterHeight;
            ValueAnimator anim=ValueAnimator.ofInt(height,0);
            anim.setDuration(700);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Integer value = (Integer) valueAnimator.getAnimatedValue();
                    filterLin.getLayoutParams().height = value.intValue();;
                    filterLin.setAlpha((value.intValue()*1.0f)/height);
                    filterLin.requestLayout();
                }
            });

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    filterLin.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            anim.start();
        }
        else
        {

            final int height= filterHeight;
            ValueAnimator anim=ValueAnimator.ofInt(0,height);
            anim.setDuration(700);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Integer value = (Integer) valueAnimator.getAnimatedValue();
                    filterLin.getLayoutParams().height = value.intValue();;
                    filterLin.setAlpha((value.intValue()*1.0f)/height);
                    filterLin.requestLayout();
                }
            });

            filterLin.setVisibility(View.VISIBLE);
            anim.start();
        }








    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_filter) {
            filterToggle();
        } else if (id == R.id.nav_fav) {
            Log.e("fav click","yes");
            new HttpGetAsync("http://dcpenterprise.co.in/auddy/movie_week.php", new AsyncCallback() {
                @Override
                public void onStart() {

                }

                @Override
                public void onFinish(String jsonReceive) {
                    if(jsonReceive!=null)
                    {
                        try{
                            JSONObject jsonObject=new JSONObject(jsonReceive);
                            Intent i =new Intent(HomeActivity.this,MovieDetails.class);
                            Bundle b=new Bundle();
                            b.putString("movie_id",jsonObject.getString("movie_id"));
                            i.putExtras(b);
                            startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("dcp json error",e.getMessage());
                        }
                    }
                }
            }).execute();

        } else if (id == R.id.nav_about_us) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dcpsoft.com")));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
