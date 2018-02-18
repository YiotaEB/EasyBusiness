package com.easybusiness.eb_androidapp.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.easybusiness.eb_androidapp.Model.AppMode;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Fragments.AdminHomeFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.ViewCustomersFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.ViewEmployeesFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.ViewInventoryFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.ViewProductionFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.ViewSalesFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.ViewSuppliersFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String APP_MODE_STRING = "APP_MODE";

    private Toolbar toolBar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private AppMode appMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the activity mode.
        appMode = (AppMode) getIntent().getSerializableExtra(APP_MODE_STRING);

        //Set layouts and menu according to application mode.
        switch (appMode) {
            case MODE_USER:
                setContentView(R.layout.activity_main_user);
                navigationView = findViewById(R.id.nav_view_user);
                break;
            case MODE_ADMIN:
                setContentView(R.layout.activity_main_admin);
                navigationView = findViewById(R.id.nav_view_admin);
                break;
            default:
                throw new RuntimeException("Invalid application mode selected");
        }


        //Set layouts and menus according to the appliclation mode.
        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        showDefaultFragment();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else {
            FragmentManager manager = getSupportFragmentManager();
            if (manager.getBackStackEntryCount() > 0) {
                super.onBackPressed();
                Fragment currentFragment = manager.findFragmentById(R.id.frame);

                //TODO: Is it needed?
//                if (currentFragment.getClass() == AdminHomeFragment.class /*|| currentFragment.getClass() == UserHomeFragment.class*/)
//                    navigationView.getMenu().getItem(0).setChecked(true);
//                else if (currentFragment.getClass() == AddEmployeesFragment.class)
//                    navigationView.getMenu().getItem(1).setChecked(true);

            }
            else super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.action_settings:
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame);
        Fragment newFragment = null;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left);

        switch (id) {

            //HOME:
            case R.id.admin_nav_home:
                setTitle(AdminHomeFragment.TITLE);
                newFragment = new AdminHomeFragment();
                fragmentTransaction.replace(R.id.frame, newFragment, AdminHomeFragment.TAG);
                break;

            //INVENTORY:
            case R.id.admin_nav_inventory:
                setTitle(ViewInventoryFragment.TITLE);
                newFragment = new ViewInventoryFragment();
                fragmentTransaction.replace(R.id.frame, newFragment, ViewInventoryFragment.TAG);
                break;

            //SALES:
            case R.id.admin_nav_sales:
                setTitle(ViewSalesFragment.TITLE);
                newFragment = new ViewSalesFragment();
                fragmentTransaction.replace(R.id.frame, newFragment, ViewSalesFragment.TAG);
                break;

            //PRODUCTION:
            case R.id.admin_nav_production:
                setTitle(ViewProductionFragment.TITLE);
                newFragment = new ViewProductionFragment();
                fragmentTransaction.replace(R.id.frame, newFragment, ViewProductionFragment.TAG);
                break;

            //SUPPLIERS:
            case R.id.admin_nav_suppliers:
                setTitle(ViewSuppliersFragment.TITLE);
                newFragment = new ViewSuppliersFragment();
                fragmentTransaction.replace(R.id.frame, newFragment, ViewSuppliersFragment.TAG);
                break;

            //CUSTOMERS:
            case R.id.admin_nav_customers:
                setTitle(ViewCustomersFragment.TITLE);
                newFragment = new ViewCustomersFragment();
                fragmentTransaction.replace(R.id.frame, newFragment, ViewCustomersFragment.TAG);
                break;

            //EMPLOYEES:
            case R.id.admin_nav_employees:
                setTitle(ViewEmployeesFragment.TITLE);
                newFragment = new ViewEmployeesFragment();
                fragmentTransaction.replace(R.id.frame, newFragment, ViewEmployeesFragment.TAG);
                break;

        }

        if (newFragment != null && newFragment.getClass() != currentFragment.getClass()) {
            fragmentTransaction.addToBackStack(newFragment.getTag());
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showDefaultFragment() {
        Fragment defaultFragment = new AdminHomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        setTitle(AdminHomeFragment.TITLE);
        fragmentTransaction.replace(R.id.frame, defaultFragment, AdminHomeFragment.TAG);
        fragmentTransaction.commit();
        navigationView.getMenu().getItem(0).setChecked(true);
    }

}
