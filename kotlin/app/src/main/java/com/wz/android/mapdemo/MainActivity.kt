/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */
package com.wz.android.mapdemo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.*

/**
 * map activity entrance class
 */
class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        private const val TAG = "MapViewDemoActivity"
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        private const val REQUEST_CODE = 100
        private val LAT_LNG = LatLng(31.2304, 121.4737)
        private val RUNTIME_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET)
    }

    private lateinit var hmap: HuaweiMap
    private lateinit var mMapView: MapView
    private var mMarker: Marker? = null
    private var mCircle: Circle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "map onCreate:")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!hasPermissions(this, *RUNTIME_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, RUNTIME_PERMISSIONS, REQUEST_CODE)
        }

        // get mapView by layout view
        mMapView = findViewById(R.id.mapView)
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        // please replace "Your API key" with api_key field value in
        // agconnect-services.json if the field is null.
        MapsInitializer.setApiKey("Your API key")
        mMapView.onCreate(mapViewBundle)

        // get map by async method
        mMapView.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        mMapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

    override fun onMapReady(map: HuaweiMap) {
        Log.d(TAG, "onMapReady: ")

        // after call getMapAsync method ,we can get HuaweiMap instance in this call back method
        hmap = map
        hmap.isMyLocationEnabled = true

        // move camera by CameraPosition param ,latlag and zoom params can set here
        val build = CameraPosition.Builder().target(LatLng(60.0, 60.0)).zoom(5f).build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(build)
        hmap.animateCamera(cameraUpdate)
        hmap.setMaxZoomPreference(5f)
        hmap.setMinZoomPreference(2f)

        // mark can be add by HuaweiMap
        mMarker = hmap.addMarker(MarkerOptions().position(LAT_LNG)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.badge_ph))
                .clusterable(true))
        mMarker?.showInfoWindow()

        // circle can be add by HuaweiMap
        mCircle = hmap.addCircle(CircleOptions().center(LatLng(60.0, 60.0)).radius(5000.0).fillColor(Color.GREEN))
        mCircle?.fillColor = Color.TRANSPARENT
    }

    override fun onPause() {
        mMapView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }
}