/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.wz.android.mapdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.huawei.hms.maps.CameraUpdate;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.MapsInitializer;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.huawei.hms.maps.model.CameraPosition;
import com.huawei.hms.maps.model.Circle;
import com.huawei.hms.maps.model.CircleOptions;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * map activity entrance class
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "MapViewDemoActivity";

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private static final int REQUEST_CODE = 100;

    private static final LatLng LAT_LNG = new LatLng(48.893478, 2.334595);

    private HuaweiMap hMap;

    private MapView mMapView;

    private Marker mMarker;

    private Circle mCircle;

    private static final String[] RUNTIME_PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "map onCreate:");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!hasPermissions(this, RUNTIME_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, RUNTIME_PERMISSIONS, REQUEST_CODE);
        }

        // get mapView by layout view
        mMapView = findViewById(R.id.mapView);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        // please replace "Your API key" with api_key field value in
        // agconnect-services.json if the field is null.
        MapsInitializer.setApiKey("Your API key");
        mMapView.onCreate(mapViewBundle);

        // get map by async method
        mMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(HuaweiMap map) {
        Log.d(TAG, "onMapReady: ");

        // after call getMapAsync method ,we can get HuaweiMap instance in this call back method
        hMap = map;

        if (hasPermissions(this, RUNTIME_PERMISSIONS[0], RUNTIME_PERMISSIONS[1])) {
            setLocationEnabled(true);
        }

        // move camera by CameraPosition param ,latlag and zoom params can set here
        CameraPosition build = new CameraPosition.Builder().target(LAT_LNG).zoom(10).build();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(build);
        hMap.animateCamera(cameraUpdate);

        // mark can be add by HuaweiMap
        mMarker = hMap.addMarker(new MarkerOptions().position(LAT_LNG)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.badge_ph))
                .clusterable(true));

        // circle can be add by HuaweiMap
        mCircle = hMap.addCircle(new CircleOptions().center(new LatLng(48.793478, 2.334595)).radius(1000).fillColor(Color.GREEN));
        mCircle.setFillColor(Color.TRANSPARENT);
    }


    /**
     * checkSelfPermission
     *
     * @param context     Context
     * @param permissions permissions
     * @return true or false
     */
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            setLocationEnabled(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED);
        }
    }

    /**
     * set location enable
     *
     * @param enable true:enable, false:disable
     */
    private void setLocationEnabled(boolean enable) {
        if (hMap != null) {
            hMap.setMyLocationEnabled(enable);
            hMap.getUiSettings().setMyLocationButtonEnabled(enable);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}