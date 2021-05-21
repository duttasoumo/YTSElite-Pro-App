package com.dcpsoft.ytselitepro.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcpsoft.ytselitepro.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Soumya on 10-Jul-16.
 */
public class HomeMovieAdapter extends ArrayAdapter<HomeMovieData> {

    private LayoutInflater inflater;
    private List<HomeMovieData> movie_data;
    private Context mContext;

    public HomeMovieAdapter(Context context, List<HomeMovieData> objects) {
        super(context, android.R.layout.simple_list_item_1,  objects);
        this.mContext=context;
        this.inflater=LayoutInflater.from(mContext);
        this.movie_data =objects;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v;
        ViewHolder vh;
        v=view;
        if(v==null)
        {
            v=inflater.inflate(R.layout.movie_home_grid_container,null);
            vh=new ViewHolder();
            vh.img=(ImageView)v.findViewById(R.id.container_img);
            vh.text1=(TextView)v.findViewById(R.id.container_text1);
            vh.text2=(TextView)v.findViewById(R.id.container_text2);
            vh.textRate=(TextView)v.findViewById(R.id.container_rate);
            v.setTag(vh);
        }
        else vh=(ViewHolder) v.getTag();


        Picasso.with(mContext)
                .load(movie_data.get(position).getMovieImgUrl())
                .placeholder(R.drawable.yts_loading_img)
                .into(vh.img);
        vh.text1.setText(movie_data.get(position).getMovieName());
        vh.text2.setText(movie_data.get(position).getMovieYear());
        vh.textRate.setText(" "+movie_data.get(position).getMovieRate());

        return v;
    }

    private  class ViewHolder
    {
        ImageView img;
        TextView text1,text2,textRate;
    }

}
