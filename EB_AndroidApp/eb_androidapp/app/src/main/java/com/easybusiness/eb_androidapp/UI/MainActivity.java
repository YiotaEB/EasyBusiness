package com.easybusiness.eb_androidapp.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.view.View;
import android.widget.TextView;

import com.easybusiness.eb_androidapp.AsyncTask.GetCountriesAsyncTask;
import com.easybusiness.eb_androidapp.AsyncTask.GetUserLevelsAsyncTask;
import com.easybusiness.eb_androidapp.AsyncTask.LogoutAsyncTask;
import com.easybusiness.eb_androidapp.Entities.Countries;
import com.easybusiness.eb_androidapp.Entities.Customers;
import com.easybusiness.eb_androidapp.Entities.Products;
import com.easybusiness.eb_androidapp.Entities.Suppliers;
import com.easybusiness.eb_androidapp.Entities.Supplies;
import com.easybusiness.eb_androidapp.Entities.UserLevels;
import com.easybusiness.eb_androidapp.Entities.Users;
import com.easybusiness.eb_androidapp.Model.AppMode;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Fragments.AddCustomersFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.AddEmployeesFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.AddSuppliersFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.AdminHomeFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.ViewCustomersFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.ViewDeliveryRoutesFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.ViewEmployeesFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.ViewInventoryFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.ViewProductionFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.ViewRoutesFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.ViewSalesFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.ViewSuppliersFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String APP_MODE_STRING = "APP_MODE";
    public static final String PREFERENCE_USERNAME = "preference-username";
    public static final String PREFERENCE_PASSWORD_HASH = "preference-password-hash";
    public static final String PREFERENCE_FIRSTNAME = "preference-firstname";
    public static final String PREFERENCE_LASTNAME = "preference-lastname";
    public static final String PREFERENCE_SESSIONID = "preference-sessionID";
    public static final String PREFERENCE_USERLEVELID = "preference-userelevelID";

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private AppMode appMode;

    //Fragments:
    public static AddCustomersFragment addCustomersFragment = null;
    public static  AddEmployeesFragment addEmployeesFragment = null;
    public static  AddSuppliersFragment addSuppliersFragment = null;
    public static  AdminHomeFragment adminHomeFragment = null;
    public static  ViewCustomersFragment viewCustomersFragment = null;
    public static  ViewDeliveryRoutesFragment viewDeliveryRoutesFragment = null;
    public static  ViewEmployeesFragment viewEmployeesFragment = null;
    public static  ViewInventoryFragment viewInventoryFragment = null;
    public static  ViewProductionFragment viewProductionFragment = null;
    public static  ViewRoutesFragment viewRoutesFragment = null;
    public static  ViewSalesFragment viewSalesFragment = null;
    public static  ViewSuppliersFragment viewSuppliersFragment = null;

    private String currentUserFirstname;
    private String currentUserLastname;
    public static String currentUserLevelID;


    public ArrayList<Customers> CUSTOMERS_DATA = new ArrayList<>();
    public ArrayList<Suppliers> SUPPLIER_DATA = new ArrayList<>();
    public ArrayList<Supplies> SUPPLY_DATA = new ArrayList<>();
    public ArrayList<Products> PRODUCT_DATA = new ArrayList<>();
    public ArrayList<Users> EMPLOYEES_DATA = new ArrayList<>();
    public ArrayList<UserLevels> USERLEVELS_DATA = new ArrayList<>();
    public ArrayList<Countries> COUNTRY_DATA = new ArrayList<>();

    private View navHeader;
    private TextView nameTextView;
    public static TextView userLevelTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        currentUserFirstname = sharedPreferences.getString(PREFERENCE_FIRSTNAME, "None");
        currentUserLastname = sharedPreferences.getString(PREFERENCE_LASTNAME, "None");
        currentUserLevelID = sharedPreferences.getString(PREFERENCE_USERLEVELID, "-1");

        //Get User Levels
        new GetUserLevelsAsyncTask("SessionID=" + PreferenceManager.getDefaultSharedPreferences(this).getString(PREFERENCE_SESSIONID, ""), this, navigationView).execute();
        //Get Countries
        new GetCountriesAsyncTask("SessionID=" + PreferenceManager.getDefaultSharedPreferences(this).getString(PREFERENCE_SESSIONID, ""), this, navigationView).execute();

        appMode = (AppMode) getIntent().getSerializableExtra(APP_MODE_STRING);

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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //----------------------
        navHeader = navigationView.getHeaderView(0);
        nameTextView = navHeader.findViewById(R.id.drawer_name);
        userLevelTextView = navHeader.findViewById(R.id.drawer_userlevel);


        String nameString = currentUserFirstname + " " + currentUserLastname;
        nameTextView.setText(nameString);
        userLevelTextView.setText(currentUserLevelID); //TODO Actual name of user level.

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
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack();

                //setMenuItemChecked(newFragment);

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

            //logs the user out from the system
            case R.id.action_logout:
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                String sessionId = sharedPreferences.getString(PREFERENCE_SESSIONID, "0");
                final LogoutAsyncTask logoutAsyncTask = new LogoutAsyncTask(this,findViewById(R.id.content_main),sessionId );
                logoutAsyncTask.execute();
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
        fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);

        switch (id) {

            //HOME:
            case R.id.admin_nav_home:
                setTitle(AdminHomeFragment.TITLE);
                if (adminHomeFragment == null) {
                    newFragment = new AdminHomeFragment();
                }
                else {
                    newFragment = adminHomeFragment;
                }
                fragmentTransaction.replace(R.id.frame, newFragment, AdminHomeFragment.TAG);
                break;

            //INVENTORY:
            case R.id.admin_nav_inventory:
                setTitle(ViewInventoryFragment.TITLE);
                if (viewInventoryFragment == null) {
                    newFragment = new ViewInventoryFragment();
                }
                else {
                    newFragment = viewInventoryFragment;
                }
                fragmentTransaction.replace(R.id.frame, newFragment, ViewInventoryFragment.TAG);
                break;

            //SALES:
            case R.id.admin_nav_sales:
                setTitle(ViewSalesFragment.TITLE);
                if (viewSalesFragment == null) {
                    newFragment = new ViewSalesFragment();
                }
                else {
                    newFragment = viewSalesFragment;
                }
                fragmentTransaction.replace(R.id.frame, newFragment, ViewSalesFragment.TAG);
                break;

            //PRODUCTION:
            case R.id.admin_nav_production:
                setTitle(ViewProductionFragment.TITLE);
                if (viewProductionFragment == null) {
                    newFragment = new ViewProductionFragment();
                }
                else {
                    newFragment = viewProductionFragment;
                }
                fragmentTransaction.replace(R.id.frame, newFragment, ViewProductionFragment.TAG);
                break;

            //SUPPLIERS:
            case R.id.admin_nav_suppliers:
                setTitle(ViewSuppliersFragment.TITLE);
                if (viewSuppliersFragment == null) {
                    newFragment = new ViewSuppliersFragment();
                }
                else {
                    newFragment = viewSuppliersFragment;
                }
                fragmentTransaction.replace(R.id.frame, newFragment, ViewSuppliersFragment.TAG);
                break;

            //CUSTOMERS:
            case R.id.admin_nav_customers:
                setTitle(ViewCustomersFragment.TITLE);
                if (viewCustomersFragment == null) {
                    newFragment = new ViewCustomersFragment();
                }
                else {
                    newFragment = viewCustomersFragment;
                }
                fragmentTransaction.replace(R.id.frame, newFragment, ViewCustomersFragment.TAG);
                break;

            //EMPLOYEES:
            case R.id.admin_nav_employees:
                setTitle(ViewEmployeesFragment.TITLE);
                if (viewEmployeesFragment == null) {
                    newFragment = new ViewEmployeesFragment();
                }
                else {
                    newFragment = viewEmployeesFragment;
                }
                fragmentTransaction.replace(R.id.frame, newFragment, ViewEmployeesFragment.TAG);
                break;

        }

        if (newFragment != null && newFragment.getClass() != currentFragment.getClass()) {
            fragmentTransaction.addToBackStack(newFragment.getTag());
            fragmentTransaction.commit();
        }

        setMenuItemChecked(newFragment);

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

    //TODO:
    public void setMenuItemChecked(Fragment fragment) {
        switch (appMode) {
            case MODE_ADMIN:
                break;
            case MODE_USER:
                break;
        }
    }

    public String getUserLevelNameFromID(int id) {
        if (USERLEVELS_DATA == null)  return String.valueOf(id);
        for (int i = 0; i < USERLEVELS_DATA.size(); i++) {
            if (USERLEVELS_DATA.get(i).getUserLevelID() == id) {
                return USERLEVELS_DATA.get(i).getUserLevelName();
            }
        }
        return "Missing User Level Name";
    }

    public String getCountryFromCountryID(int id) {
        if (COUNTRY_DATA == null)  return String.valueOf(id);
        for (int i = 0; i < COUNTRY_DATA.size(); i++) {
            if (COUNTRY_DATA.get(i).getID()==id) {
                return COUNTRY_DATA.get(i).getName();
            }
        }
        return "Missing Country Name";
    }

}
