package org.svuonline.anaskoara.webid.loginregistration.models;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.svuonline.anaskoara.webid.loginregistration.R;

import java.util.ArrayList;

/**
 * Created by anaskoara on 12/11/2017.
 */

public class MyProductAdapter extends ProductAdapter {
    public MyProductAdapter(Context context, ArrayList<Product> products) {
        super(context, products);
    }


    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        // Get view for row item
        //View rowView = mInflater.inflate(R.layout.item,viewGroup,false);

        //TextView tv_name =
        //      (TextView) rowView.findViewById();


        ViewHolder holder;
        if(convertView == null) {
            convertView = getmInflater().inflate(R.layout.my_item, viewGroup, false);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_intitialPrice=(TextView) convertView.findViewById(R.id.tv_initial_price);
            holder.tv_maxPid=(TextView) convertView.findViewById(R.id.tv_max_bid);
            holder.tv_user=(TextView) convertView.findViewById(R.id.tv_user);
            holder.marker=(TextView) convertView.findViewById(R.id.marker) ;
            holder.tv_price_note=(TextView) convertView.findViewById(R.id.tv_price_note) ;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        TextView tv_name = holder.tv_name;
        TextView tv_initialPrice = holder.tv_intitialPrice;
        TextView tv_maxBid = holder.tv_maxPid;
        TextView tv_user = holder.tv_user;
        TextView marker=holder.marker;
        TextView tv_price_note=holder.tv_price_note;
        Product p=products.get(i);
        if(p.isSold()){
            marker.setBackgroundColor(Color.GREEN);
            tv_price_note.setText("Sold for: ");
        }else{
            tv_price_note.setText("Max Bid: ");
        }
        tv_name.setText(p.getName());
        tv_initialPrice.setText(String.valueOf(p.getInitialPrice()));
        tv_maxBid.setText(String.valueOf(p.getMaxBid()));
        String user=p.getUser()!=null?p.getUser().getName():"";
       // tv_user.setText(user);
        return convertView;
    }


    private static class ViewHolder {
        public TextView tv_name;
        public TextView tv_intitialPrice;
        public TextView tv_maxPid;
        public TextView tv_user;
        public TextView marker;
        public TextView tv_price_note;
    }
}