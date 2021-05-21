package com.dcpsoft.ytselitepro.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcpsoft.ytselitepro.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Soumya on 07-Aug-16.
 */
public class CastAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<CastData> mCastData;
    private Context mContext;

    public CastAdapter(Context context, List<CastData> mCastData) {

        inflater = LayoutInflater.from(context);
        this.mCastData = mCastData;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mCastData.size();
    }

    @Override
    public Object getItem(int i) {
        return mCastData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        View view=convertView;
        final ViewHolder vh;
        if(view==null){
            view= inflater.inflate(R.layout.cast_list_item,null);
            vh=new ViewHolder();
            vh.castImg =(ImageView) view.findViewById(R.id.cast_img);
            vh.castName = (TextView)view.findViewById(R.id.cast_actor_txt);
            vh.castCharName = (TextView)view.findViewById(R.id.cast_character_txt);
            view.setTag(vh);
        }
        else vh=(ViewHolder) view.getTag();

        if(mCastData.get(position).getImg().length()>0)
            Picasso.with(mContext).load(mCastData.get(position).getImg()).into(vh.castImg, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    vh.castImg.setImageResource(R.drawable.ic_small_no_pic);
                }
            });
        vh.castName.setText(mCastData.get(position).getOrgName());
        vh.castCharName.setText(mCastData.get(position).getCharacterName());

        return view;
    }

    private class ViewHolder{
        private ImageView castImg;
        private TextView castName,castCharName;
    }
}
