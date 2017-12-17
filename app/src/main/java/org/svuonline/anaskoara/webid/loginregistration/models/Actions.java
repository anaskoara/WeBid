package org.svuonline.anaskoara.webid.loginregistration.models;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import org.svuonline.anaskoara.webid.loginregistration.MainActivity;
import org.svuonline.anaskoara.webid.loginregistration.MyProducts;
import org.svuonline.anaskoara.webid.loginregistration.R;

/**
 * Created by anaskoara on 12/8/2017.
 */

public class Actions implements NavigationView.OnNavigationItemSelectedListener {

    private final Activity activity;


    public Actions(Activity activity){
        this.activity=activity;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_my_products) {
            Intent mainIntent = new Intent(activity, MyProducts.class);

            activity.startActivity(mainIntent);

        } else if (id == R.id.nav_logout) {

            MyApplication app = (MyApplication) activity.getApplicationContext();
            app.setUser(null);
            app.setLoggedIn(false);

            Intent mainIntent = new Intent(activity, MainActivity.class);

            activity.startActivity(mainIntent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





}
