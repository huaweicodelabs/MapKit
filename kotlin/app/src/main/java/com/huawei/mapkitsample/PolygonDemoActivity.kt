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
 *  2020.1.3-Changed modify the import classes type and add some polygon display demos.
 *                  Huawei Technologies Co., Ltd.
 *
 */

package com.huawei.mapkitsample

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.SupportMapFragment
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.Polygon
import com.huawei.hms.maps.model.PolygonOptions
import com.huawei.mapkitsample.utils.CheckUtils.checkIsEdit
import com.huawei.mapkitsample.utils.CheckUtils.checkIsRight
import com.huawei.mapkitsample.utils.MapUtils
import com.huawei.mapkitsample.utils.Utill.toast
import kotlinx.android.synthetic.main.activity_polygon_demo.*

/**
 * about polygon
 */
class PolygonDemoActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mSupportMapFragment: SupportMapFragment? = null
    private var hMap: HuaweiMap? = null
    private var mPolygon: Polygon? = null
    companion object {
        private const val TAG = "PolygonDemoActivity"
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_polygon_demo)
        val fragment =
            supportFragmentManager.findFragmentById(R.id.mapInPolygon)
        if (fragment is SupportMapFragment) {
            mSupportMapFragment = fragment
            mSupportMapFragment?.getMapAsync(this)
        }
        /**
         * Add polygons to the map
         */
        btnAddPolygon.setOnClickListener {
            addPolygon()
        }
        /**
         * Remove the polygon
         */
        btnRemovePolygon.setOnClickListener {
            mPolygon?.remove()
        }
        /**
         * Set the point position information of the polygon
         */
        btnSetPoints.setOnClickListener {
            setPoints()
        }
        /**
         * Get the point position information of the polygon
         */
        btnGetPoints.setOnClickListener {
            getPoints()
        }

        /**
         * Set the outline color of the polygon
         */
        btnSetStokeColor.setOnClickListener {
            mPolygon?.strokeColor = Color.YELLOW

        }

        /**
         * Get the outline color of the polygon
         */
        btnGetStokeColor.setOnClickListener {
            polygonShown.text = "${getString(R.string.PolygonColorText)}${Integer.toHexString(mPolygon!!.strokeColor)}"

        }
        /**
         * Set the fill color of the polygon
         */
        btnSetFill.setOnClickListener {
            mPolygon?.fillColor = Color.CYAN
        }
        /**
         * Get the fill color of the polygon
         */
        btnGetFill.setOnClickListener {
            polygonShown.text = "${getString(R.string.PolygonColorText)}${Integer.toHexString(mPolygon!!.fillColor)}"

        }
        /**
         * Set the tag of the polygon
         */
        btnSetTag.setOnClickListener {
            setTag()
        }
        /**
         * Get the tag of the polygon
         */
        btnGetTag.setOnClickListener {
            polygonShown?.text =
                if (mPolygon?.tag == null) "Tag is null" else mPolygon?.tag.toString()
        }
        /**
         * Add polygon click event
         */
        btnAddClickEvent.setOnClickListener {
            addClickEvent()
        }
        /**
         * Set polygons clickable
         */
        btnSetClickableTrue.setOnClickListener {
            mPolygon?.isClickable = true
        }
        /**
         * Set polygons are not clickable
         */
        btnSetClickableFalse.setOnClickListener {
            mPolygon?.isClickable = false
        }
    }

    override fun onMapReady(paramHuaweiMap: HuaweiMap) {
        Log.i(TAG, "onMapReady: ")
        hMap = paramHuaweiMap
        hMap?.apply {
            isMyLocationEnabled = true
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(48.893478, 2.334595), 10f))
        }
    }
    fun addPolygon() {
        mPolygon?.remove()
        mPolygon = hMap?.addPolygon(
            PolygonOptions().addAll(MapUtils.createRectangle(LatLng(48.893478, 2.334595), 0.1, 0.1))
                .fillColor(Color.GREEN)
                .strokeColor(Color.BLACK)
        )
        hMap?.setOnPolygonClickListener { Log.i(TAG, "addPolygon and onPolygonClick start ") }
    }

    fun setPoints() {
            val latitude = oneLatitude?.text.toString().trim()
            val longitude = oneLongtitude?.text.toString().trim()
            if (checkIsEdit(latitude) || checkIsEdit(longitude)) {
                toast("Please make sure the latitude & longitude is Edited")
            } else {
                if (!checkIsRight(latitude) || !checkIsRight(longitude)) {
                    toast("Please make sure the latitude & longitude is right")
                } else {
                    mPolygon?.points = MapUtils
                        .createRectangle(
                            LatLng(
                                java.lang.Double.valueOf(latitude),
                                java.lang.Double.valueOf(longitude)
                            ), 0.5, 0.5
                        )
                }
             }
    }
    private fun getPoints() {
            val stringBuilder = StringBuilder()
            for (i in mPolygon?.points?.toTypedArray()!!.indices) {
                stringBuilder.append(mPolygon!!.points[i].toString())
            }
            toast("Polygon points is $stringBuilder")
    }
    private fun setTag() {
            val tag = polygonTag.text.toString().trim()
            if (checkIsEdit(tag)) {
                toast("Please make sure the width is Edited")
            } else {
                mPolygon?.tag = tag
            }
    }
    private fun addClickEvent() {
            hMap?.setOnPolygonClickListener {
                toast("Polygon is clicked.")
            }
    }
}