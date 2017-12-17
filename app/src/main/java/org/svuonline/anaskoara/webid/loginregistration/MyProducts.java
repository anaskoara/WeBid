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
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.svuonline.anaskoara.webid.loginregistration.models.Actions;
import org.svuonline.anaskoara.webid.loginregistration.models.MyApplication;
import org.svuonline.anaskoara.webid.loginregistration.models.MyProductAdapter;
import org.svuonline.anaskoara.webid.loginregistration.models.Product;
import org.svuonline.anaskoara.webid.loginregistration.models.User;
import org.svuonline.anaskoara.webid.loginregistration.server.ProductInterface;
import org.svuonline.anaskoara.webid.loginregistration.server.ProductRequest;
import org.svuonline.anaskoara.webid.loginregistration.server.ProductResponse;
import org.svuonline.anaskoara.webid.loginregistration.server.RequestInterface;
import org.svuonline.anaskoara.webid.loginregistration.server.ServerRequest;
import org.svuonline.anaskoara.webid.loginregistration.server.ServerResponse;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyProducts extends AppCompatActivity {

    private MyProductAdapter adapter;
    private EndlessScrollListener scrollListener;
    private User user;
    private EditText et_product_name;
    private EditText et_init_price;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);

        MyApplication app = (MyApplication) getApplicationContext();
        user=app.getUser();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_product);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddProductDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new Actions(this));
        //code start here
        final Context context = this;
        ListView lv_items=(ListView) findViewById(R.id.lv_my_items);
        //final EditText search=(EditText) findViewById(R.id.search);

        ArrayList<Product> products=new ArrayList<>();//=getProducts("",1);
        fetchProducts("",1);
        adapter=new MyProductAdapter(this,products);
        //  ArrayAdapter< String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,array);

        lv_items.setAdapter(adapter);
        lv_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Product selectedProduct = adapter.get(position);
                Intent detailIntent = new Intent(context, MyProductDetailActivity.class);
                int productId=selectedProduct.getId();
                detailIntent.putExtra("id",productId);
                startActivity(detailIntent);
            }


        });

        scrollListener=new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                fetchProducts("", page);
                adapter.notifyDataSetChanged();
                return true;
            }
        };
        lv_items.setOnScrollListener(scrollListener);


       /**search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                resetData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/
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
        getMenuInflater().inflate(R.menu.my_products, menu);
        return true;
    }

    /*@Override
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
    }*/



    private void fetchProducts(String term, final int page) {
        ArrayList<Product>products=new ArrayList<Product>();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);


        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.PRODUCTLIST);
        HashMap<String,String> parameters=new HashMap<>();

        parameters.put("page",Integer.toString(page));
        parameters.put("term",term);

        request.setUser(user);
        request.setParameters(parameters);

        Call<ServerResponse> response = requestInterface.operation(request);


        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                String  x=response.body().toString();
                ServerResponse resp = response.body();


                if( resp.getResult().equals(Constants.SUCCESS)){


                    for(Product p:resp.getProducts()){
                        adapter.add(p);
                    }
                    adapter.notifyDataSetChanged();


                }
                // progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                adapter.add(new Product(0,t.getMessage()));
                //Log.d(Constants.TAG,"failed");
                //Snackbar.make(context, t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });

    }
    public void resetData(){
        // 1. First, clear the array of data
        adapter.clear();
// 2. Notify the adapter of the update
        adapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
// 3. Reset endless scroll listener when performing a new search
        scrollListener.resetState();
    }

    private void showAddProductDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add_product, null);
        et_product_name = (EditText)view.findViewById(R.id.et_product_name);
        et_init_price = (EditText)view.findViewById(R.id.et_initial_price);
//        progress = (ProgressBar)view.findViewById(R.id.progress);
        builder.setView(view);
        builder.setTitle("Add new Product To Sell.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // Snackbar.make(view,"sdsdsdsdfsdfsdfd",Snackbar.LENGTH_LONG);
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

                int initialPrice=Integer.valueOf(et_init_price.getText ().toString());
                addProduct(et_product_name.getText().toString(),initialPrice);
                dialog.dismiss();
            }
        });

    }

    private void addProduct(String productName , int initialPrice) {
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
        request.setOperation(Constants.ADD_PRODUCT);

        Product p=new Product();
        p.setName(productName);
        p.setInitialPrice(initialPrice);
        p.setUser(user);
        request.setProduct(p);

        Call<ProductResponse> response = requestInterface.operation(request);


        response.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, retrofit2.Response<ProductResponse> response) {
                String  x=response.body().toString();
                ProductResponse resp = response.body();


                if( resp.getResult().equals(Constants.SUCCESS)){
                    Intent intent=new Intent(MyProducts.this,MyProducts.class);
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
}
