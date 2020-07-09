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
 *  2020.1.3-Changed modify the import classes type and add some groundOverlay demos.
 *                  Huawei Technologies Co., Ltd.
 *
 */

package com.huawei.mapkitsample

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.SupportMapFragment
import com.huawei.hms.maps.UiSettings
import com.huawei.hms.maps.util.LogM
import com.huawei.mapkitsample.utils.Utill.toast
import kotlinx.android.synthetic.main.activity_gestures_demo.*

/**
 * about gesture
 */
class GestureDemoActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mSupportMapFragment: SupportMapFragment
    private var hMap: HuaweiMap? = null
    private var mUiSettings: UiSettings? = null

    companion object {
        private const val TAG = "GestureDemoActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestures_demo)
        mSupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapInGestures) as SupportMapFragment
        mSupportMapFragment.getMapAsync(this)

        /**
         * Set map zoom button available
         */
        isShowZoomButton.setOnClickListener{
            if (checkReady()) {
                mUiSettings?.isZoomControlsEnabled = isShowZoomButton.isChecked
                toast("isZoomControlsEnabled ${isShowZoomButton.isChecked}")
            }
            else
                toast("Please wait.. MapView is not ready yet.")
        }
        /**
         * Set compass available
         */
        isShowCompass.setOnClickListener{
            if (checkReady()) {
                mUiSettings?.isCompassEnabled = isShowCompass.isChecked
                toast("isCompassEnabled ${isShowCompass.isChecked}")
            }
            else
                toast("Please wait.. MapView is not ready yet.")
        }
        /**
         * Set my location button is available
         */
        isShowMylocationButton.setOnClickListener{
            setMyLocationButtonEnabled()
        }
        /**
         * Set my location layer available
         */
        isMyLocationLayerEnabled.setOnClickListener{
            setMyLocationLayerEnabled()
        }
        /**
         * Set scroll gestures available
         */
        isScrollGesturesEnabled.setOnClickListener{
            if (checkReady()) {
                mUiSettings?.isScrollGesturesEnabled = isScrollGesturesEnabled.isChecked
                toast("isScrollGesturesEnabled ${isScrollGesturesEnabled.isChecked}")

            }
            else
                toast("Please wait.. MapView is not ready yet.")
        }
        /**
         * Set zoom gestures available
         */
        isZoomGesturesEnabled.setOnClickListener{
            if (checkReady()) {
                mUiSettings?.isZoomGesturesEnabled = isZoomGesturesEnabled.isChecked
                toast("isZoomGesturesEnabled ${isZoomGesturesEnabled.isChecked}")

            }
            else
                toast("Please wait.. MapView is not ready yet.")
        }
        /**
         * Set tilt gestures available
         */
        isTiltGesturesEnabled.setOnClickListener{
            if (checkReady()) {
                mUiSettings?.isTiltGesturesEnabled = isTiltGesturesEnabled.isChecked
                toast("isTiltGesturesEnabled ${isTiltGesturesEnabled.isChecked}")
            }
            else
                toast("Please wait.. MapView is not ready yet.")
        }
        /**
         * Set the rotation gesture available
         */
        isRotateGesturesEnabled.setOnClickListener{
            if (checkReady()) {
                mUiSettings?.isRotateGesturesEnabled = isRotateGesturesEnabled.isChecked
                toast("isRotateGesturesEnabled ${isRotateGesturesEnabled.isChecked}")
            }
            else
                toast("Please wait.. MapView is not ready yet.")
        }
    }

    override fun onMapReady(paramHuaweiMap: HuaweiMap) {
        LogM.i(TAG, "onMapReady: ")
        hMap = paramHuaweiMap
        hMap?.apply {
            isMyLocationEnabled = false
            uiSettings?.apply {
                isCompassEnabled = false
                isZoomControlsEnabled = false
                isMyLocationButtonEnabled = false
            }
        }
        mUiSettings = hMap?.uiSettings
    }

    private fun checkReady(): Boolean {
        if (hMap == null) {
            toast( "Map is not ready yet")
            return false
        }
        return true
    }
    private fun setMyLocationButtonEnabled() {
        if (!checkReady()) {
            return
        }
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (isMyLocationLayerEnabled.isChecked) {
                mUiSettings?.isMyLocationButtonEnabled = isShowMylocationButton.isChecked
                toast("isMyLocationButtonEnabled ${isShowMylocationButton.isChecked}")


            } else {
                toast("Please open My Location Layer first")
                isShowMylocationButton.isChecked = false
            }
        } else {
            toast("System positioning permission was not obtained, please turn on system positioning permission first")
            isShowMylocationButton.isChecked = false
        }
    }

    private fun setMyLocationLayerEnabled() {
        if (!checkReady()) {
            return
        }
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            hMap?.isMyLocationEnabled = isMyLocationLayerEnabled.isChecked
            toast("isMyLocationEnabled ${isMyLocationLayerEnabled.isChecked}")

        } else {
            isMyLocationLayerEnabled.isChecked = false
        }
    }

}