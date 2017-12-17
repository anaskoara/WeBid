package org.svuonline.anaskoara.webid.loginregistration;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.svuonline.anaskoara.webid.loginregistration.models.BidOffer;
import org.svuonline.anaskoara.webid.loginregistration.models.MyApplication;
import org.svuonline.anaskoara.webid.loginregistration.models.Product;
import org.svuonline.anaskoara.webid.loginregistration.models.User;
import org.svuonline.anaskoara.webid.loginregistration.server.ProductInterface;
import org.svuonline.anaskoara.webid.loginregistration.server.ProductRequest;
import org.svuonline.anaskoara.webid.loginregistration.server.ProductResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Product product;
    private TextView productName;
    private TextView initialPrice;
    private TextView maxPid;
    private TextView user;
    private Context context;
    private ListView pids;
    private TextView tv_max;
    private EditText et_bid;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.bt_sell);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        productName= (TextView) findViewById(R.id.tv_product_name);
        initialPrice=(TextView) findViewById(R.id.tv_initial_price);
        user=(TextView) findViewById(R.id.tv_user);
        maxPid=(TextView) findViewById(R.id.tv_max_bid);
        pids=(ListView) findViewById(R.id.lv_bids);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
// get data via the key
        int id = extras.getInt("id");
        fetchProduct(id);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.logout) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void fetchProduct(int id) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ProductInterface requestInterface = retrofit.create(ProductInterface.class);


        ProductRequest request = new ProductRequest();
        request.setOperation(Constants.PRODUCTDETAIL);
        request.setId(id);
        Call<ProductResponse> response = requestInterface.operation(request);


        response.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, retrofit2.Response<ProductResponse> response) {
                String  x=response.body().toString();
                ProductResponse resp = response.body();


                if( resp.getResult().equals(Constants.SUCCESS)){
                    product=resp.getProduct();

                    productName.setText(product.getName());
                    user.setText(product.getUser().getName());
                    initialPrice.setText(String.valueOf(product.getInitialPrice()));
                    maxPid.setText(String.valueOf(product.getMaxBid()));
                    BidAdapter adapter=new BidAdapter(context,product.getBids());
                    pids.setAdapter(adapter);

                    adapter.notifyDataSetChanged();


                }
                // progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {

                //adapter.add(new Product(0,t.getMessage()));
                //Log.d(Constants.TAG,"failed");
                //Snackbar.make(context, t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });

    }

    private void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add_bid, null);
        tv_max = (TextView)view.findViewById(R.id.tv_max);
        tv_max.setText(product.getMaxBid()+"");
        et_bid = (EditText)view.findViewById(R.id.et_bid);
//        progress = (ProgressBar)view.findViewById(R.id.progress);
        builder.setView(view);
        builder.setTitle("Place  Pid");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(view,"sdsdsdsdfsdfsdfd",Snackbar.LENGTH_LONG);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               int bid=Integer.valueOf(et_bid.getText().toString());
                if(bid>product.getMaxBid()){
                    makeBid(bid);
                    dialog.dismiss();
                }
            }
        });

    }

    private void makeBid(int bid) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ProductInterface requestInterface = retrofit.create(ProductInterface.class);

        MyApplication app = (MyApplication) getApplicationContext();
        User user=app.getUser();

        ProductRequest request = new ProductRequest();
        request.setOperation(Constants.Make_BID);

        request.setId(product.getId());
        request.setUser(user);
        request.setBid(bid);

        Call<ProductResponse> response = requestInterface.operation(request);


        response.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, retrofit2.Response<ProductResponse> response) {
                String  x=response.body().toString();
                ProductResponse resp = response.body();


                if( resp.getResult().equals(Constants.SUCCESS)){
                    Intent intent=new Intent(context,ProductDetailActivity.class);
                    intent.putExtra("id",product.getId());
                    startActivity(intent);

                }
                // progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {

                //adapter.add(new Product(0,t.getMessage()));
                //Log.d(Constants.TAG,"failed");
                //Snackbar.make(context, t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    static class BidAdapter extends BaseAdapter {


        Context context;
        private final LayoutInflater mInflater;
        List<BidOffer> bids=new ArrayList<>();

        public BidAdapter(Context context, List<BidOffer> bids) {
            this.context = context;
            this.bids = bids;
            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return bids.size();
        }

        @Override
        public Object getItem(int i) {
            return bids.get(i);
        }

        @Override
        public long getItemId(int i) {
            return  bids.get(i).getId();
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {

            // Get view for row item
            //View rowView = mInflater.inflate(R.layout.item,viewGroup,false);

            //TextView tv_name =
            //      (TextView) rowView.findViewById();


            BidAdapter.ViewHolder holder;
            if(convertView == null) {
                convertView = mInflater.inflate(R.layout.bid_item, viewGroup, false);
                holder = new BidAdapter.ViewHolder();
                holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
                holder.tv_user=(TextView) convertView.findViewById(R.id.tv_user);

                convertView.setTag(holder);
            }
            else{
                holder = (BidAdapter.ViewHolder) convertView.getTag();
            }

            TextView tv_price = holder.tv_price;
            TextView tv_user = holder.tv_user;
            BidOffer p=bids.get(i);

            tv_price.setText(String.valueOf(p.getOfferedPrice()));
            String user=p.getUser()!=null?p.getUser().getName():"";
            tv_user.setText(user);
            return convertView;
        }



        public BidOffer get(int position) {
            return  bids.get(position);
        }

        private static class ViewHolder {
            public TextView tv_price;
            public TextView tv_user;
        }


        public void clear(){bids.clear();}
    }
}
