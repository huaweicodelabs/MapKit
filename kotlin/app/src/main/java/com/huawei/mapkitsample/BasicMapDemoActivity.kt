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
 *  2020.1.3-Changed modify the import classes type and add some map display demos.
 *                  Huawei Technologies Co., Ltd.
 *
 */

package com.huawei.mapkitsample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.activity_basic_demo.*

// This class shows how we create a basic activity with a map.
class BasicMapDemoActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener{
    private var hmap: HuaweiMap? = null
    companion object {
        private const val TAG = "BasicMapDemoActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_demo)
        btnMapView.setOnClickListener(this)
        btnMapViewCode.setOnClickListener(this)
        btnMapFragment.setOnClickListener(this)
        btnMapFragmentCode.setOnClickListener(this)
        btnSupportMapFragment.setOnClickListener(this)
        btnSupportMapFragmentCode.setOnClickListener(this)

    }
    override fun onMapReady(map: HuaweiMap) {
        Log.d(TAG, "onMapReady: ")
        hmap = map
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnMapView -> {
                startActivity<MapViewDemoActivity>(this)
                Log.d(TAG, "createMapView: ")
            }
            R.id.btnMapViewCode -> {
                Log.d(TAG, "createMapViewCode: ")
                startActivity<MapViewCodeDemoActivity>(this)            }
            R.id.btnMapFragment -> {
                Log.d(TAG, "createMapFragment: ")
                startActivity<MapFragmentDemoActivity>(this)            }
            R.id.btnMapFragmentCode-> {
                Log.d(TAG, "createMapFragmentCode: ")
                startActivity<MapFragmentCodeDemoActivity>(this)
            }
            R.id.btnSupportMapFragment->{
                Log.d(TAG, "createSupportMapFragment: ")
                startActivity<SupportMapDemoActivity>(this)
            }
            R.id.btnSupportMapFragmentCode->{
                Log.d(TAG, "createSupportMapFragmentCode: ")
                startActivity<SupportMapCodeDemoActivity>(this)
            }
            else -> {
            }
        }
    }
    private inline fun <reified T:Activity> startActivity(context: Context)
    {
        startActivity(Intent(context, T::class.java))
    }

}