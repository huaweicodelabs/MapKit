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
 *  2020.1.3-Changed modify the import classes type and add some polyline demos.
 *                  Huawei Technologies Co., Ltd.
 *
 */

package com.huawei.mapkitsample

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.SupportMapFragment
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.Polyline
import com.huawei.hms.maps.model.PolylineOptions
import com.huawei.hms.maps.util.LogM
import com.huawei.mapkitsample.R.string.TagNullText
import com.huawei.mapkitsample.utils.CheckUtils.checkIsEdit
import com.huawei.mapkitsample.utils.CheckUtils.checkIsRight
import com.huawei.mapkitsample.utils.MapUtils
import com.huawei.mapkitsample.utils.Utill.toast
import kotlinx.android.synthetic.main.activity_polyline_demo.*
import java.util.*

/**
 * about polyline
 */
class PolylineDemoActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mSupportMapFragment: SupportMapFragment
    private var hMap: HuaweiMap? = null
    private var mPolyline: Polyline? = null
    private val points: MutableList<LatLng> = ArrayList()

    companion object {
        private const val TAG = "PolylineDemoActivity"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_polyline_demo)
        mSupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.mapInPolyline) as SupportMapFragment
        mSupportMapFragment.getMapAsync(this)
        points.apply {
            add(MapUtils.HUAWEI_CENTER)
            add(MapUtils.APARTMENT_CENTER)
            add(MapUtils.EPARK_CENTER)
        }

        /**
         * Add polyline to the map
         */
        btnAddPolyline.setOnClickListener {
            addPolyline()
        }
        /**
         * Remove the polyline
         */
        btnRemovePolyline.setOnClickListener {
            removePolyline()
        }
        /**
         * Set the point position information of the polyline
         */
        btnSetOnePoint.setOnClickListener {
            setOnePoint()
        }
        /**
         * Get the point position information of the polyline
         */
        btnGetPoints.setOnClickListener {
            getPoints()
        }
        /**
         * Set the width of the polyline
         */
        btnSetWidth.setOnClickListener {
            setWidth()
        }
        /**
         * Get the width of the polyline
         */
        btnGetWidth.setOnClickListener {
            polylineShown.text = "${getString(R.string.CircleWidthText)}${mPolyline?.width}"

        }
        /**
         * Set the outline color of the polyline
         */
        btnSetStoke.setOnClickListener {
            mPolyline?.color = Color.YELLOW

        }
        /**
         * Get the outline color of the polyline
         */
        btnGetStoke.setOnClickListener {
            mPolyline?.let {
                val color=mPolyline?.color
                    color?.let {
                        polylineShown.text =
                            "${getString(R.string.PolygonColorText)}${Integer.toHexString(color!!)}"
                    }
            }
        }
        /**
         * Set the tag of the polyline
         */
        btnSetTag.setOnClickListener {
            setTag()
        }
        /**
         * Get the tag of the polyline
         */
        btnGetTag.setOnClickListener {
            polylineShown?.text = if (mPolyline?.tag == null) getString(TagNullText) else mPolyline?.tag.toString()
        }
        /**
         * Add polyline click event
         */
        btnAddClickEvent.setOnClickListener {
            hMap?.setOnPolylineClickListener {
                toast("Polyline is clicked.")
            }
        }
        /**
         * Set polyline clickable
         */
        btnSetClickableTrue.setOnClickListener {
            mPolyline?.isClickable = true
        }
        /**
         * Set polyline are not clickable
         */
        btnSetClickableFalse.setOnClickListener {
            mPolyline?.isClickable = false
        }
    }

    override fun onMapReady(paramHuaweiMap: HuaweiMap) {
        LogM.i(TAG, "onMapReady: ")
        hMap = paramHuaweiMap
        hMap?.apply {
            isMyLocationEnabled = true
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(48.893478, 2.334595), 10f))
        }
    }

    private fun addPolyline() {
        mPolyline?.remove()

        mPolyline = hMap?.addPolyline(
            PolylineOptions().add(
                MapUtils.FRANCE,
                MapUtils.FRANCE1,
                MapUtils.FRANCE2,
                MapUtils.FRANCE3
            )
              .color(Color.BLUE)
              .width(3f)
        )
        hMap?.setOnPolylineClickListener { LogM.i(TAG, "onMapReady:onPolylineClick ") }
    }
    private fun removePolyline() {
        mPolyline?.remove()
        points.apply {
            clear()
            add(MapUtils.HUAWEI_CENTER)
            add(MapUtils.APARTMENT_CENTER)
            add(MapUtils.EPARK_CENTER)
        }
    }
    private fun setOnePoint() {
            val latitude = oneLatitude?.text.toString().trim()
            val longitude = oneLongtitude?.text.toString().trim()
            if (checkIsEdit(latitude) || checkIsEdit(longitude)) {
                toast("Please make sure the latitude & longitude is Edited")
            } else {
                if (!checkIsRight(latitude) || !checkIsRight(longitude)) {
                    toast("Please make sure the latitude & longitude is right")
                } else {
                    points.add(
                        LatLng(
                            latitude.toDouble(),
                            longitude.toDouble()
                        )
                    )
                    mPolyline?.points = points
                }
            }
    }
    private fun getPoints() {
            val stringBuilder = StringBuilder()
            for (i in mPolyline!!.points.toTypedArray().indices) {
                stringBuilder.append(mPolyline!!.points[i].toString())
            }
            toast("Polyline points is $stringBuilder")
    }

    private fun setWidth() {
            val width = polylineStokeWidth?.text.toString().trim()
            if (checkIsEdit(width)) {
                toast("Please make sure the width is Edited")
            } else {
                if (!checkIsRight(width)) {
                    toast("Please make sure the width is right")
                } else {
                    mPolyline?.width = width.toFloat()
                }
            }
    }
    private fun setTag() {
            val tag = polylineTag?.text.toString().trim()
            if (checkIsEdit(tag)) {
                toast("Please make sure the width is Edited")
            } else {
                mPolyline?.tag = tag
            }
    }

}