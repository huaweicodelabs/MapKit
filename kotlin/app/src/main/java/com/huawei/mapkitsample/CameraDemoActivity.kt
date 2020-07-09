/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *  2020.1.3-Changed modify the import classes type and add some camera events.
 *                  Huawei Technologies Co., Ltd.
 *
 */

package com.huawei.mapkitsample

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.HuaweiMap.*
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.SupportMapFragment
import com.huawei.hms.maps.model.CameraPosition
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.LatLngBounds
import com.huawei.hms.maps.util.LogM
import com.huawei.mapkitsample.utils.Utill.toast
import kotlinx.android.synthetic.main.activity_camera_demo.*


//* Show how to move a map camera
class CameraDemoActivity : AppCompatActivity(), OnMapReadyCallback, OnCameraMoveStartedListener, OnCameraMoveListener,
    OnCameraIdleListener {
    private lateinit var mSupportMapFragment: SupportMapFragment
    private var hMap: HuaweiMap? = null
    private var mMaxZoom = 22.0f
    private var mMinZoom = 0.0f

    companion object {
        private const val TAG = "CameraDemoActivity"
        val PERMISSION = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        const val REQUEST_CODE = 0X01
        private const val ZOOM_DELTA = 2.0f
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_demo)
        if (!hasLocationPermission()) {
            ActivityCompat.requestPermissions(this, PERMISSION, REQUEST_CODE)
        }
        val fragment =
            supportFragmentManager.findFragmentById(R.id.mapInCamera)
        if (fragment is SupportMapFragment) {
            mSupportMapFragment = fragment
            mSupportMapFragment.getMapAsync(this)
        }
        animateCamera.setOnClickListener{
            val cameraUpdate = CameraUpdateFactory.newLatLng(LatLng(20.0, 120.0))
            toast(hMap?.cameraPosition?.target.toString())
            hMap?.animateCamera(cameraUpdate)
        }
        getCameraPosition.setOnClickListener{
            val position = hMap?.cameraPosition
            toast(position.toString())
            // Displays the maximum zoom level and minimum scaling level of the current camera.
            LogM.i(TAG, position.toString())
            LogM.i(
                TAG,
                "MaxZoomLevel:" + hMap?.maxZoomLevel + " MinZoomLevel:" + hMap?.minZoomLevel
            )
        }
        moveCamera.setOnClickListener{
            val build = CameraPosition.Builder().target(LatLng(60.0, 60.0)).build()
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(build)
            toast(hMap?.cameraPosition.toString())
            hMap?.moveCamera(cameraUpdate)
        }
        ZoomBy.setOnClickListener{
            val cameraUpdate = CameraUpdateFactory.zoomBy(2f)
            toast("amount = 2")
            hMap?.moveCamera(cameraUpdate)
        }
        newLatLngBounds.setOnClickListener{
            val southwest = LatLng(30.0, 60.0)
            val northeast = LatLng(60.0, 120.0)
            val latLngBounds = LatLngBounds(southwest, northeast)
            toast("southwest =$southwest northeast=$northeast padding=2")
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, 2)
            hMap?.moveCamera(cameraUpdate)
        }
        setCameraPosition.setOnClickListener{
            val southwest = LatLng(30.0, 60.0)
            val cameraPosition =
                CameraPosition.builder().target(southwest).zoom(10f).bearing(2.0f).tilt(2.5f)
                    .build()
            toast(cameraPosition.toString())
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            hMap?.moveCamera(cameraUpdate)
        }
        //* Set the lower limit of camera zoom
        setMinZoomClamp.setOnClickListener {
            mMinZoom += ZOOM_DELTA
            // Constrains the minimum zoom level.
            hMap?.setMinZoomPreference(mMinZoom)
            toast("Min zoom preference set to: $mMinZoom")
        }
        //* Set the upper limit of camera zoom
        setMaxZoomClamp.setOnClickListener {
            mMaxZoom -= ZOOM_DELTA
            // Constrains the maximum zoom level.
            hMap?.setMaxZoomPreference(mMaxZoom)
            toast("Max zoom preference set to: $mMaxZoom")

        }
        setCamera.setOnClickListener {
            setCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    toast("permission setting successfully")
                } else {
                    toast("permission setting failed")
                    finish()
                }
            }
        }
    }

    // * Determine if you have the location permission
    private fun hasLocationPermission(): Boolean {
        for (permission in PERMISSION) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    // * Determine whether to turn on GPS
    private fun isGPSOpen(context: Context): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val network =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gps || network
    }

    override fun onMapReady(huaweiMap: HuaweiMap) {
        LogM.i(TAG, "onMapReady: ")
        hMap = huaweiMap
        if (isGPSOpen(this)) {
            hMap?.apply {
                isMyLocationEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
            }
        } else {
            hMap?.apply {
                isMyLocationEnabled = false
                uiSettings.isMyLocationButtonEnabled = false
                setOnCameraMoveStartedListener(this@CameraDemoActivity)
            }
        }
        hMap?.apply {
            setOnCameraMoveStartedListener(this@CameraDemoActivity)
            setOnCameraIdleListener(this@CameraDemoActivity)
            setOnCameraMoveListener(this@CameraDemoActivity)
            setOnMapLoadedCallback { LogM.i(TAG, "onMapLoaded:successful") }
        }
    }

    // * Callback when the camera starts moving
    override fun onCameraMoveStarted(reason: Int) {
        LogM.i(TAG, "onCameraMoveStarted: successful")
        if (reason == OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION) {
            LogM.i(TAG, "onCameraMove")
        }
    }

    //* Callback when the camera move ends
    override fun onCameraIdle() {
        cameraChange.text = hMap?.cameraPosition.toString()
        LogM.i(TAG, "onCameraIdle: successful")
    }

    //* Set camera move callback
    override fun onCameraMove() {
        LogM.i(TAG, "onCameraMove: successful")
    }

    //* Set the map camera based on the latitude and longitude, zoom factor, tilt angle, and rotation angle
    private fun setCamera() {
        try {
            var latLng: LatLng? =null
            var zoom = 2.0f
            var bearing = 2.0f
            var tilt = 2.0f
            if (cameraLat.text.isNotEmpty() && cameraLng.text.isNotEmpty()) {
                latLng =
                    LatLng((cameraLat.text.toString().trim()).toDouble(),
                            (cameraLng.text.toString().trim()).toDouble()
                    )
            }
            else{
                cameraLat.error="cameraLat and cameraLng should not be blank"

            }
            if (cameraZoom.text.isNotEmpty()) {
                zoom = (cameraZoom.text.toString().trim()).toFloat()
            }
            else{
                cameraZoom.error="cameraZoom should not be blank"
            }
            if (cameraBearing.text.isNotEmpty()) {
                bearing = (cameraBearing.text.toString().trim()).toFloat()
            }
            else{
                cameraBearing.error="cameraBearing should not be blank"
            }
            if (cameraTilt.text.isNotEmpty()) {
                tilt = (cameraTilt.text.toString().trim()).toFloat()
            }
            else{
                cameraTilt.error="cameraTilt should not be blank"
            }

            val cameraPosition =
                CameraPosition.builder().target(latLng).zoom(zoom).bearing(bearing).tilt(tilt)
                    .build()
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            hMap?.moveCamera(cameraUpdate)
        } catch (e: IllegalArgumentException) {
            LogM.e(TAG, "IllegalArgumentException $e")
            toast("IllegalArgumentException")
        } catch (e: NullPointerException) {
            LogM.e(TAG, "NullPointerException $e")
            toast("NullPointerException")
        }
    }


}