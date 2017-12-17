package org.svuonline.anaskoara.webid.loginregistration;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.svuonline.anaskoara.webid.loginregistration.models.BidOffer;
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

public class MyProductDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView productName;
    private ListView pids;
    private TextView user;
    private TextView initialPrice;
    private TextView maxPid;
    private AlertDialog dialog;
    private Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.bt_sell);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

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
        getMenuInflater().inflate(R.menu.my_product_detail, menu);
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

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        //final View view = inflater.inflate(R.layout.dialog_sell, null);
       // builder.setView(view);
        builder.setTitle("Are You Sure You Want to sell This Product?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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
                sellProduct();
                Intent intent=new Intent(MyProductDetailActivity.this,MyProductDetailActivity.class);
                intent.putExtra("id",product.getId());
                startActivity(intent);
            }
        });

    }

    private void sellProduct() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ProductInterface requestInterface = retrofit.create(ProductInterface.class);
        ProductRequest request = new ProductRequest();
        request.setOperation(Constants.SELLPRODUCT);
        request.setId(product.getId());
        Call<ProductResponse> response = requestInterface.operation(request);


        response.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, retrofit2.Response<ProductResponse> response) {
                String  x=response.body().toString();
                ProductResponse resp = response.body();
                if( resp.getResult().equals(Constants.SUCCESS)){

                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                //adapter.add(  new Product(0,t.getMessage()));
                //Log.d(Constants.TAG,"failed");
                //Snackbar.make(context, t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
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
                    Product product=resp.getProduct();
                    setProduct(product);
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                t.printStackTrace();
                //adapter.add(  new Product(0,t.getMessage()));
                //Log.d(Constants.TAG,"failed");
                //Snackbar.make(context, t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });

    }

    private void setProduct(Product product) {
        this.product=product;
        TextView name=(TextView)findViewById(R.id.tv_product_name);
        name.setText(product.getName());
        TextView initialPrice=(TextView)findViewById(R.id.tv_initial_price);
        initialPrice.setText(String.valueOf(product.getInitialPrice()));

        if(product.isSold()){
            View unSoldLayout=findViewById(R.id.ly_not_sold);
            ((ViewGroup)unSoldLayout.getParent()).removeView(unSoldLayout);

            User user=product.getBuyer();

            TextView userName= (TextView) findViewById(R.id.name);
            userName.setText(user.getName());
            TextView login= (TextView) findViewById(R.id.login);
            login.setText(user.getLogin());
            TextView tel= (TextView) findViewById(R.id.tel);
            tel.setText(user.getTel());

            TextView price= (TextView) findViewById(R.id.final_price);
            price.setText(String.valueOf(product.getMaxBid()));

        }else{
            View soldLayout = findViewById(R.id.ly_sold);
            ((ViewGroup)soldLayout.getParent()).removeView(soldLayout);

            TextView maxBid= (TextView) findViewById(R.id.tv_max_bid);

                maxBid.setText(String.valueOf(product.getMaxBid()));

        }

        pids=(ListView)findViewById(R.id.lv_bids);
        BidAdapter adapter=new BidAdapter(this,product.getBids());
        pids.setAdapter(adapter);

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
