package org.svuonline.anaskoara.webid.loginregistration;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;

import org.svuonline.anaskoara.webid.loginregistration.models.MyApplication;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private NavigationView menuLayout ;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        initFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void initFragment() {
        Fragment fragment;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.appLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        menuLayout = (NavigationView)findViewById(R.id.menu_layout);
        MyApplication app = (MyApplication) getApplicationContext();
        boolean isloggedin = app.isLoggedIn();


        if (isloggedin) {
            viewMenu(true);
            Intent intent=new Intent(this,ProductsActivity.class);
            startActivity(intent);
        } else{
           viewMenu(false);
            fragment = new LoginFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_frame, fragment);
            ft.commit();
        }

    }


    public void viewMenu(boolean view){
        if(view){
            if(menuLayout.getParent()!=null){
                mDrawerLayout.removeView(menuLayout);
            }
            mDrawerLayout.addView(menuLayout);
            getSupportActionBar().show();
        }else{

            mDrawerLayout.removeView(menuLayout);
            getSupportActionBar().hide();
        }
    }



}
