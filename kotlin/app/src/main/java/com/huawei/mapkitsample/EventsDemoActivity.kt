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
 *  2020.1.3-Changed modify the import classes type and add some events demo.
 *                  Huawei Technologies Co., Ltd.
 *
 */

package com.huawei.mapkitsample

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.HuaweiMap.*
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.SupportMapFragment
import com.huawei.hms.maps.model.LatLng
import com.huawei.mapkitsample.R.string
import com.huawei.mapkitsample.R.string.istappedText
import com.huawei.mapkitsample.utils.Utill.toast
import kotlinx.android.synthetic.main.activity_events_demo.*

/**
 * This shows how to listen to some HuaweiMap events
 */
class EventsDemoActivity : AppCompatActivity(), OnMapClickListener,
    OnMapLongClickListener, OnCameraIdleListener, OnMapReadyCallback,
    OnMyLocationButtonClickListener {
    private var hMap: HuaweiMap? = null
    private lateinit var mSupportMapFragment: SupportMapFragment

    companion object {
        private const val TAG = "EventsDemoActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_demo)
        this.mSupportMapFragment = this.supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mSupportMapFragment.getMapAsync(this)

    }

    override fun onMapReady(map: HuaweiMap) {
        hMap = map
        hMap?.apply {
            setOnMapClickListener(this@EventsDemoActivity)
            setOnMapLongClickListener(this@EventsDemoActivity)
            setOnCameraIdleListener(this@EventsDemoActivity)
            setOnMyLocationButtonClickListener(this@EventsDemoActivity)
            isMyLocationEnabled = true
        }
    }

    /**
     * Map click event
     */
    @SuppressLint("SetTextI18n")
    override fun onMapClick(latLng: LatLng) {
        tapText.text = "${getString(string.pointText)}${latLng}${getString(istappedText)}}"
        val point = hMap?.projection?.toScreenLocation(latLng)
        toPoint.text = "${getString(string.pointToPoint)}$point"
        val newLatlng = hMap?.projection?.fromScreenLocation(point)
        toLatlng.text = "${getString(string.toLatLngText)}$newLatlng"
        val visibleRegion = hMap?.projection?.visibleRegion
        Log.i(TAG, visibleRegion.toString())
    }

    /**
     * Map long click event
     */
    @SuppressLint("SetTextI18n")
    override fun onMapLongClick(point: LatLng) {
        tapText.text = "${getString(string.longPressedText)}$point"
    }

    /**
     * Callback when the camera move ends
     */
    override fun onCameraIdle() {
        toast("camera move stop")
    }

    /**
     * Map click event
     */
    override fun onMyLocationButtonClick(): Boolean {
        toast("MyLocation button clicked")
        return false
    }
}