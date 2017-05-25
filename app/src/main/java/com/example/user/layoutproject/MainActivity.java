package com.example.user.layoutproject;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.user.layoutproject.Task.TestingTask;
import com.example.user.layoutproject.fragment.BookParsorFragment;
import com.example.user.layoutproject.fragment.FullScannerFragment;
import com.example.user.layoutproject.fragment.ParsorFragment;
import com.example.user.layoutproject.fragment.TrackingFragment;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FullScannerFragment.FragmentTransitionListener {

    private FrameLayout contentFrame;
    private FragmentManager fragmentManager;
    private FloatingActionMenu menuRed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contentFrame = (FrameLayout) findViewById(R.id.main_frameLayout);
        fragmentManager = getSupportFragmentManager();

        menuRed = (FloatingActionMenu) findViewById(R.id.menu_red);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullScannerFragment fullScannerFragment = new FullScannerFragment();
                fullScannerFragment.setTransitionTrigger(MainActivity.this);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.main_frameLayout, fullScannerFragment, "fullscanner").commit();
                menuRed.close(true);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.nav_trace_parsor) {
            ParsorFragment parsorFragment = new ParsorFragment();
            transition(parsorFragment, "parsorFragment");
        } else if (id == R.id.nav_trace_list) {
            TrackingFragment trackingFragment = new TrackingFragment();
            transition(trackingFragment, "trackingFragment");
        } else if (id == R.id.nav_books_parsor) {
            BookParsorFragment bookParsorFragment = new BookParsorFragment();
            transition(bookParsorFragment, "bookParsorFragment");
        } else if (id == R.id.nav_books_list) {

        } else if (id == R.id.nav_share) {
            new TestingTask().execute();
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void transition(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frameLayout, fragment, tag).commit();
    }
}
