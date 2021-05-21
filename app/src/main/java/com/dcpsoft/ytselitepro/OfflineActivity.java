package com.dcpsoft.ytselitepro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Soumya on 10-Sep-16.
 */
public class OfflineActivity extends AppCompatActivity {

    private FancyButton retryBut;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        retryBut = (FancyButton)findViewById(R.id.retry_btn);
        retryBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try{
                    String tmp=getIntent().getExtras().getString("movie_id");
                    Intent i=new Intent(OfflineActivity.this,MovieDetails.class);
                    Bundle b=new Bundle();
                    b.putString("movie_id",tmp);
                    i.putExtras(b);
                    startActivity(i);
                    OfflineActivity.this.finish();
                }
                catch (NullPointerException e)
                {
                    startActivity(new Intent(OfflineActivity.this,HomeActivity.class));
                    OfflineActivity.this.finish();
                }
            }
        });

    }
}
