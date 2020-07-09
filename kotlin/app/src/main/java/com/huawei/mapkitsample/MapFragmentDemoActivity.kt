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
 *  2020.1.3-Changed modify the import classes type and add some mapFragment demos.
 *                  Huawei Technologies Co., Ltd.
 *
 */

package com.huawei.mapkitsample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.LatLng

class MapFragmentDemoActivity : AppCompatActivity(), OnMapReadyCallback {
    private var hMap: HuaweiMap? = null
    private lateinit var mSupportMapFragment: SupportMapFragment
    companion object {
        private const val TAG = "MapFragmentDemoActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapfragment_demo)
        mSupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mSupportMapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: HuaweiMap) {
        Log.d(TAG, "onMapReady: ")
        hMap = map
        hMap?.apply {
            uiSettings?.isMyLocationButtonEnabled = false
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(48.893478, 2.334595), 10f))
        }
    }

}