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

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.huawei.hms.maps.util.LogM
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , View.OnClickListener {

    companion object {
        private const val TAG = "MainActivity"
        private val RUNTIME_PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET
        )
        private const val REQUEST_CODE = 100
        private fun hasPermissions(context: Context, vararg permissions: String): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (permission in permissions) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            permission
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return false
                    }
                }
            }
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!hasPermissions(
                this,
                *RUNTIME_PERMISSIONS
            )
        ) {
            ActivityCompat.requestPermissions(
                this,
                RUNTIME_PERMISSIONS,
                REQUEST_CODE
            )
        }

        btnCamera.setOnClickListener(this)
        btnBasicMap.setOnClickListener(this)
        btnGestureDemo.setOnClickListener(this)
        btnControlsDemo.setOnClickListener(this)
        btnCircleDemo.setOnClickListener(this)
        btnPolygonDemo.setOnClickListener(this)
        btnPolylineDemo.setOnClickListener(this)
        btnGroudOverlayDemo.setOnClickListener(this)
        btnLiteModeDemo.setOnClickListener(this)
        btnMoreLanguageDemo.setOnClickListener(this)
        btnMapFunctions.setOnClickListener(this)
        btnAddMarkerDemo.setOnClickListener(this)
        btnEventsDemo.setOnClickListener(this)
        btnMapStyle.setOnClickListener(this)
        btnLocationSourceDemo.setOnClickListener(this)
        btnRoutePlanningDemo.setOnClickListener(this)
    }

    private inline fun <reified T : Activity> startActivity(context: Context) {
        startActivity(Intent(context, T::class.java))
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnCamera -> {
                LogM.i(TAG, "onClick: cameraDemo")
                startActivity<CameraDemoActivity>(this)
            }
            R.id.btnBasicMap -> {
                LogM.i(TAG, "onClick: BasicMap")
                startActivity<BasicMapDemoActivity>(this)
            }
            R.id.btnGestureDemo -> {
                LogM.i(TAG, "onClick: GestureDemoActivity")
                startActivity<GestureDemoActivity>(this)
            }
            R.id.btnControlsDemo -> {
                LogM.i(TAG, "onClick: ControlsDemoActivity")
                startActivity<ControlsDemoActivity>(this)
            }
            R.id.btnCircleDemo -> {
                LogM.i(TAG, "onClick: CircleDemoActivity")
                startActivity<CircleDemoActivity>(this)
            }
            R.id.btnPolygonDemo -> {
                LogM.i(TAG, "onClick: PolygonDemoActivity")
                startActivity<PolygonDemoActivity>(this)
            }

            R.id.btnPolylineDemo -> {
                LogM.i(TAG, "onClick: PolylineDemoActivity")
                startActivity<PolylineDemoActivity>(this)
            }
            R.id.btnGroudOverlayDemo -> {
                LogM.i(TAG, "onClick: GroundOverlayDemoActivity")
                startActivity<GroundOverlayDemoActivity>(this)
            }
            R.id.btnLiteModeDemo -> {
                LogM.i(TAG, "onClick: LiteModeDemoActivity")
                startActivity<LiteModeDemoActivity>(this)
            }
            R.id.btnMoreLanguageDemo -> {
                LogM.i(TAG, "onClick: MoreLanguageDemoActivity")
                startActivity<MoreLanguageDemoActivity>(this)
            }
            R.id.btnMapFunctions -> {
                LogM.i(TAG, "onClick: MapFunctionsDemoActivity")
                startActivity<MapFunctionsDemoActivity>(this)
            }
            R.id.btnAddMarkerDemo -> {
                LogM.i(TAG, "onClick: AddMarkerDemo")
                startActivity<MarkerDemoActivity>(this)
            }

            R.id.btnEventsDemo -> {
                LogM.i(TAG, "onClick: EventsDemo")
                startActivity<EventsDemoActivity>(this)
            }
            R.id.btnMapStyle -> {
                LogM.i(TAG, "onClick: StyleMapDemoActivity")
                startActivity<StyleMapDemoActivity>(this)
            }
            R.id.btnLocationSourceDemo -> {
                LogM.i(TAG, "onClick: LocationSourceDemo")
                startActivity<LocationSourceDemoActivity>(this)
            }
            R.id.btnRoutePlanningDemo -> {
                LogM.i(TAG, "onClick: RoutePlanningDemoActivity")
                startActivity<RoutePlanningDemoActivity>(this)
            }
        }
    }
}