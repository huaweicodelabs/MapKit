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
 *  2020.1.3-Changed modify the import classes type and add some map function demos.
 *                  Huawei Technologies Co., Ltd.
 *
 */

package com.huawei.mapkitsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.SupportMapFragment
import com.huawei.hms.maps.util.LogM
import com.huawei.mapkitsample.utils.MapUtils
import com.huawei.mapkitsample.utils.Utill.toast
import kotlinx.android.synthetic.main.acitivity_map_founctions_demo.*

/**
 * Basic functions
 */
class MapFunctionsDemoActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mSupportMapFragment: SupportMapFragment? = null
    private var hMap: HuaweiMap? = null

    companion object {
        private const val TAG = "MapFunctionsDemoActivity"
        fun isNumber(value: String): Boolean {
            return isInteger(value) || isDouble(value)
        }
        /**
         * Determine if the string is an integer
         */
        private fun isInteger(value: String): Boolean {
            return try {
                value.toInt()
                true
            } catch (e: NumberFormatException) {
                false
            }
        }
        /**
         * Determine if the string is a float
         */
        private fun isDouble(value: String): Boolean {
            return try {
                value.toDouble()
                value.contains(".")
            } catch (e: NumberFormatException) {
                false
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_map_founctions_demo)
        mSupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.mapInFunctions) as SupportMapFragment?
        mSupportMapFragment?.getMapAsync(this)

        /**
         * Get the maximum zoom level parameter
         */
        btnGetMaxZoomLevel.setOnClickListener {
            founctionsshow?.text = hMap?.maxZoomLevel.toString()
        }
        /**
         * Get the minimum zoom level parameter
         */
        btnGetMinZoomLevel.setOnClickListener {
            founctionsshow?.text = hMap?.minZoomLevel.toString()
        }

        /**
         * Get map type
         */
        btnGetMapType.setOnClickListener {
            founctionsshow?.text =
                if (hMap?.mapType == MapUtils.MAP_TYPE_NONE) "MAP_TYPE_NONE" else "MAP_TYPE_NORMAL"
        }
        /**
         * Set map type
         */
        btnSetMapType.setOnClickListener {
            setMapType()
        }
        /**
         * Get 3D mode settings
         */
        btnIs3DMode.setOnClickListener {
            founctionsshow?.text = hMap?.isBuildingsEnabled.toString()
        }

        /**
         * Turn on the 3D switch
         */
        btnSet3DMode.setOnClickListener {
            hMap?.isBuildingsEnabled = !hMap!!.isBuildingsEnabled
        }
        /**
         * Set the maximum value of the desired camera zoom level
         */
        btnSetMaxZoomPreference.setOnClickListener {
           setMaxZoomPreference()
        }
        /**
         * Set the minimum value of the desired camera zoom level
         */
        btnSetMinZoomPreference.setOnClickListener {
            setMinZoomPreference()
        }
        /**
         * Remove the previously set zoom level upper and lower boundary values
        */
        btnResetMinMaxZoomPreference.setOnClickListener {
            hMap?.resetMinMaxZoomPreference()
        }
        /**
         * Set the map border fill width for the map
         */
        btnSetPadding.setOnClickListener {
            setPadding()
        }
        /**
         * Test the maximum zoom parameter
         */
        btnTestMaxZoom.setOnClickListener {
            val cameraUpdate = CameraUpdateFactory.zoomBy(1.0f)
            hMap?.moveCamera(cameraUpdate)
        }

    }

    override fun onMapReady(paramHuaweiMap: HuaweiMap) {
        LogM.i(TAG, "onMapReady: ")
        hMap = paramHuaweiMap
        hMap?.apply {
            isMyLocationEnabled = true
            resetMinMaxZoomPreference()
        }
    }
    private fun setMapType() {
            synchronized(hMap!!) {
                if (hMap?.mapType == MapUtils.MAP_TYPE_NORMAL) {
                    hMap?.mapType = HuaweiMap.MAP_TYPE_NONE
                } else {
                    hMap?.mapType = HuaweiMap.MAP_TYPE_NORMAL
                }
            }
    }
    private fun setMaxZoomPreference() {
        val text = maxZoomlevel.text.toString()
        if (text.trim().isEmpty() || text.trim().isEmpty() || "" == text) {
            maxZoomlevel.error = "Please make sure the maxZoom is Edited"
        } else {
            if ((text.trim()).toFloat() > MapUtils.MAX_ZOOM_LEVEL || (text.trim()).toFloat() < MapUtils.MIN_ZOOM_LEVEL || !isNumber(
                    text.trim())) {
                maxZoomlevel.error = "Please make sure the maxZoom is right"
            } else {
                val maxZoom = (maxZoomlevel?.text.toString()).toFloat()
                LogM.i(TAG, "setMaxZoomPreference: $maxZoom")
                    hMap?.let {
                        hMap?.setMaxZoomPreference(maxZoom)
                    }
            }
        }
    }
    private fun setMinZoomPreference() {
        val text = minZoomlevel?.text.toString()
        if (text.trim().isEmpty() || text.trim().isEmpty() || "" == text) {
            minZoomlevel.error = "Please make sure the minZoom is Edited"
        } else {
            if ((text.trim()).toFloat() > MapUtils.MAX_ZOOM_LEVEL ||(text.trim()).toFloat() < MapUtils.MIN_ZOOM_LEVEL || !isNumber(
                    text.trim()
                )
            ) {
                minZoomlevel.error = "Please make sure the minZoom is right"
            } else {
                    hMap?.let {
                        hMap?.setMinZoomPreference((minZoomlevel?.text.toString()).toFloat())
                    }
            }
        }
    }
    private fun setPadding() {
        val leftString = paddingleft.text.toString()
        val topString = paddingtop?.text.toString()
        val rightString = paddingright.text.toString()
        val bottomString = paddingbottom.text.toString()
        if (leftString.trim().isEmpty() || leftString.trim().isEmpty() || "" == leftString || topString.trim().isEmpty() || topString.trim().isEmpty() || "" == topString || rightString.trim().isEmpty() || rightString.trim().isEmpty() || "" == rightString || bottomString.trim().isEmpty() || bottomString.trim().isEmpty() || "" == bottomString) {
            return
        }
        if (!isNumber(leftString.trim()) || !isNumber(
                topString.trim()
            ) || !isNumber(rightString.trim())
            || !isNumber(bottomString.trim())
        ) {
            toast("Please make sure the padding value is right")
        } else {
                hMap?.let {
                    hMap?.setPadding(
                        Integer.valueOf(paddingleft.text.toString()),
                        Integer.valueOf(paddingtop.text.toString()),
                        Integer.valueOf(paddingright.text.toString()),
                        Integer.valueOf(paddingbottom.text.toString()))
                }
            }
        }
}