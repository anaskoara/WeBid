package org.svuonline.anaskoara.webid.loginregistration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.svuonline.anaskoara.webid.loginregistration.models.Actions;
import org.svuonline.anaskoara.webid.loginregistration.models.Product;
import org.svuonline.anaskoara.webid.loginregistration.models.ProductAdapter;
import org.svuonline.anaskoara.webid.loginregistration.server.ServerRequest;
import org.svuonline.anaskoara.webid.loginregistration.server.ServerResponse;
import org.svuonline.anaskoara.webid.loginregistration.server.RequestInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductsActivity extends AppCompatActivity {

    private ProductAdapter adapter;
    private EndlessScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

       NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new Actions( this));


        final Context context = this;

        ListView lv_items=(ListView) findViewById(R.id.lv_items);
        final EditText search=(EditText) findViewById(R.id.search);

        ArrayList<Product> products=new ArrayList<>();//=getProducts("",1);
        fetchProducts("",1);
        adapter=new ProductAdapter(this,products);
        //  ArrayAdapter< String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,array);

        lv_items.setAdapter(adapter);
        lv_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Product selectedProduct = adapter.get(position);
                Intent detailIntent = new Intent(context, ProductDetailActivity.class);
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


        search.addTextChangedListener(new TextWatcher() {
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
        });

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
        if (id == R.id.nav_logout) {

        }

        return super.onOptionsItemSelected(item);
    }




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
}
