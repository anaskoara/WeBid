package org.svuonline.anaskoara.webid.loginregistration.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.svuonline.anaskoara.webid.loginregistration.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anaskoara on 12/2/2017.
 */

public class ProductAdapter extends BaseAdapter {


    private final Context mContext;
    private final LayoutInflater mInflater;
    List<Product> products=new ArrayList<>();

    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.mContext = context;
        this.products = products;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public LayoutInflater getmInflater() {
        return mInflater;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return  products.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        // Get view for row item
        //View rowView = mInflater.inflate(R.layout.item,viewGroup,false);

        //TextView tv_name =
        //      (TextView) rowView.findViewById();


        ViewHolder holder;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.item, viewGroup, false);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_intitialPrice=(TextView) convertView.findViewById(R.id.tv_initial_price);
            holder.tv_maxPid=(TextView) convertView.findViewById(R.id.tv_max_bid);
            holder.tv_user=(TextView) convertView.findViewById(R.id.tv_user);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        TextView tv_name = holder.tv_name;
        TextView tv_initialPrice = holder.tv_intitialPrice;
        TextView tv_maxBid = holder.tv_maxPid;
        TextView tv_user = holder.tv_user;
        Product p=products.get(i);
        tv_name.setText(p.getName());
        tv_initialPrice.setText(String.valueOf(p.getInitialPrice()));
        tv_maxBid.setText(String.valueOf(p.getMaxBid()));
        String user=p.getUser()!=null?p.getUser().getName():"";
        tv_user.setText(user);
        return convertView;
    }

    public void add(Product p) {
        this.products.add(p);
    }

    public Product get(int position) {
        return  products.get(position);
    }

    private static class ViewHolder {
        public TextView tv_name;
        public TextView tv_intitialPrice;
        public TextView tv_maxPid;
        public TextView tv_user;
    }


    public void clear(){products.clear();}
}
