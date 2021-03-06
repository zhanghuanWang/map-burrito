package com.mapzen.mapburrito.demo;

import com.mapzen.android.lost.api.LocationServices;
import com.mapzen.android.lost.api.LostApiClient;
import com.mapzen.mapburrito.MapController;

import org.oscim.android.MapView;
import org.oscim.tiling.source.OkHttpEngine;
import org.oscim.tiling.source.oscimap4.OSciMap4TileSource;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends ActionBarActivity {
    private MapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new LostApiClient.Builder(this).build().connect();

        final MapView mapView = (MapView) findViewById(R.id.map);
        mapController = new MapController(mapView.map())
                .setHttpEngine(new OkHttpEngine.OkHttpFactory())
                .setTileSource(new OSciMap4TileSource())
                .addBuildingLayer()
                .addLabelLayer()
                .setTheme("styles/default.xml");

        final ImageButton findMe = (ImageButton) findViewById(R.id.find_me);
        findMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View view) {
                centerOnCurrentLocation();
            }
        });

        final Drawable marker = getResources().getDrawable(android.R.drawable.btn_star_big_on);
        mapController.setCurrentLocationDrawable(marker);
        centerOnCurrentLocation();
    }

    private void centerOnCurrentLocation() {
        final Location location = LocationServices.FusedLocationApi.getLastLocation();
        if (location != null) {
            mapController.resetMapAndCenterOn(location).showCurrentLocation(location);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
