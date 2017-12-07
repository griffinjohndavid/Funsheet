package com.centennialdesigns.funsheet;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ListFragment.OnCardSelectedListener,
        SharedPreferences.OnSharedPreferenceChangeListener
{

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListFragment mCardsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        SharedPreferences prefs = getSharedPreferences(LoginActivity.LOGIN_PREF_NAME, 0);
        prefs.registerOnSharedPreferenceChangeListener(this);
        // Update username if logged in
        if(!prefs.getString(LoginActivity.USER_PREF_ID, "").equals("")){
            // Show current user
            View headerView = navigationView.getHeaderView(0);
            TextView navHeaderName = (TextView) headerView.findViewById(R.id.nav_header_name);
            navHeaderName.setText(prefs.getString(LoginActivity.USER_PREF_ID, ""));

            // Update login/logout
            navigationView.getMenu().findItem(R.id.nav_login_logout).setTitle("Logout");
        }


        while (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) < 0) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        mCardsListFragment = (ListFragment) fragmentManager.findFragmentById(R.id.fragment_container);

        if (mCardsListFragment == null) {
            mCardsListFragment = new ListFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, mCardsListFragment).commit();
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);

                mCardsListFragment.getCards(recyclerView, null);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetOptionSelection();
    }

    @Override
    protected void onStop() {
        super.onStop();
        GPSData.getInstance(getApplicationContext()).stopUpdatingGPS();
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
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

            if (fragment == null) {
                fragment = new ListFragment();
                fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
            }

        } else if (id == R.id.nav_random) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);

            Card card = mCardsListFragment.getRandomCard(recyclerView);
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.PARCEL_ID, card);
            startActivity(intent);

        } else if (id == R.id.nav_login_logout) {
            SharedPreferences prefs = getSharedPreferences(LoginActivity.LOGIN_PREF_NAME, 0);

            if(prefs.getString(LoginActivity.USER_PREF_ID, "").equals("")) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
            else {
                prefs.edit().remove(LoginActivity.USER_PREF_ID).commit();
            }
            resetOptionSelection();
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCardSelected(Card card) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.PARCEL_ID, card);
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if(!key.equals(LoginActivity.USER_PREF_ID))
            return;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderName = (TextView) headerView.findViewById(R.id.nav_header_name);
        MenuItem loginLogout = navigationView.getMenu().findItem(R.id.nav_login_logout);

        // Update username if logged in
        if(!prefs.getString(LoginActivity.USER_PREF_ID, "").equals("")){
            // Show current user
            navHeaderName.setText(prefs.getString(LoginActivity.USER_PREF_ID, ""));
            // Update login/logout
            loginLogout.setTitle("Logout");
        }
        else{
            navHeaderName.setText(getString(R.string.please_login));
            loginLogout.setTitle("Login");

        }

    }
    private void resetOptionSelection(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        //navigationView.getMenu().
        //navigationView.getMenu().getItem(0).setChecked(true);
    }
}
