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
 *  2020.1.3-Changed modify the import classes type and add some circle demos.
 *                  Huawei Technologies Co., Ltd.
 *
 */

package com.huawei.mapkitsample

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.SupportMapFragment
import com.huawei.hms.maps.model.Circle
import com.huawei.hms.maps.model.CircleOptions
import com.huawei.hms.maps.model.LatLng
import com.huawei.mapkitsample.R.string.*
import com.huawei.mapkitsample.utils.CheckUtils.checkIsEdit
import com.huawei.mapkitsample.utils.CheckUtils.checkIsRight
import com.huawei.mapkitsample.utils.Utill.toast
import kotlinx.android.synthetic.main.activity_circle_demo.*

/**
 * circle related
 */
class CircleDemoActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mSupportMapFragment: SupportMapFragment
    private var hMap: HuaweiMap? = null
    private var mCircle: Circle? = null

    companion object {
        private const val TAG = "CircleDemoActivity"
    }

    @SuppressLint("StringFormatInvalid", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_demo)
        mSupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapInCircle) as SupportMapFragment
        mSupportMapFragment.getMapAsync(this)

        /**
         * remove the circle
         */
        btnRemoveCircle.setOnClickListener {
            mCircle?.let {
                mCircle?.remove()
            }
        }
        /**
         * Set the center of the circle
         */
        btnSetCenter.setOnClickListener {
            setCenter()
        }
        /**
         * Get the center coordinates
         */
        btnGetCenter.setOnClickListener {
            mCircle?.let {
                circleShown.text = "${getString(circleCenterText)}${mCircle?.center}}"
            }
        }
        /**
         * add a circle on the map
         */
        btnAddCircle.setOnClickListener {
            addCircle()
        }
        /**
         * Set the radius of the circle
         */
        btnSetRadius.setOnClickListener {
            setRadius()
        }
        /**
         * Get the radius of the circle
         */
        btnGetRadius.setOnClickListener {//Circle radius is
            mCircle?.let {
                circleShown.text = "${getString(circleRadiusText)}${mCircle?.radius}}"
            }
        }
        /**
         * Set the fill color of the circle
         */
        btnSetFillColor.setOnClickListener {
            mCircle?.let {
                mCircle?.fillColor = Color.RED
                toast("set Circle Fill Color: ${mCircle?.fillColor}")
            }
        }
        /**
         * Get the fill color of the circle
         */
        btnGetFillColor.setOnClickListener {
            mCircle?.let {
                circleShown.text =
                    "${getString(circleFillColorText)}${Integer.toHexString(mCircle?.fillColor!!)}}"
            }
        }
        /**
         * Set the outline color of the circle
         */
        btnSetStokeColor.setOnClickListener {
            setStokeColor()
        }
        /**
         * Get the outline color of the circle
         */
        btnGetStokeColor.setOnClickListener {
            mCircle?.let {
                circleShown.text =
                    "${getString(circleStrokeColorText)}${Integer.toHexString(mCircle?.strokeColor!!)}}"
            }
        }
        /**
         * Set the outline width of the circle
         */
        btnSetWidth.setOnClickListener {
            setWidth()
        }
        /**
         * Get the outline width of the circle
         */
        btnGetWidth.setOnClickListener {
            mCircle?.let {
                circleShown.text = "${getString(circleStrokeWidthText)}${mCircle?.strokeWidth}}}"
            }
        }
        /**
         * Set the tag of the circle
         */
        btnSetTag.setOnClickListener {
            setTag()
        }
        /**
         * Get the tag of the circle
         */
        btnGetTag.setOnClickListener {
            mCircle?.let {
                circleShown.text = mCircle?.tag.toString()
            }
        }
        /**
         * Set click event for circle
         */
        btnAddClickEvent.setOnClickListener {
            addClickEvent()
        }
        /**
         * Set the circle clickable to true
         */
        btnSetClickableTrue.setOnClickListener {
            mCircle?.let {
                mCircle?.isClickable = true
            }
        }
        /**
         * Set the circle clickable to false
         */
        btnSetClickableFalse.setOnClickListener {
            mCircle?.let {
                mCircle?.isClickable = false
            }
        }
    }

    override fun onMapReady(paramHuaweiMap: HuaweiMap) {
        Log.i(TAG, "onMapReady: ")
        hMap = paramHuaweiMap
        hMap?.apply {
            isMyLocationEnabled = true
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(48.893478, 2.334595), 14f))
        }
    }
    private fun addCircle() {
        mCircle?.let {
            mCircle?.remove()
        }
        mCircle = hMap?.addCircle(
            CircleOptions().center(LatLng(48.893478, 2.334595))
                .radius(500.0)
                .fillColor(-0xff0001)
                .strokeWidth(10f)
                .strokeColor(Color.RED)
        )!!
    }

    private fun setCenter() {
            mCircle?.let {
            var center: LatLng? = null
            if (!TextUtils.isEmpty(centerLatitude.text) && !TextUtils.isEmpty(
                    centerLongtitude.text
                )
            ) {
                val latitude =
                    centerLatitude.text.toString().trim()
                val longitude =
                    centerLongtitude.text.toString().trim()
                center =
                    LatLng(latitude.toDouble(), longitude.toDouble())
                toast("set Circle center: ${mCircle?.center}")

            } else {
                centerLatitude.error = "Please make sure the Latitude or Longitude is Edited"
                return
            }
            mCircle?.center = center
        }

    }

    private fun setRadius() {
        mCircle?.let {
            val radius = circleRadius.text.toString().trim()
            if (checkIsEdit(radius)) {
                circleRadius.error = "Please make sure the radius is Edited"
            } else {
                if (!checkIsRight(radius)) {
                    circleRadius.error = "Please make sure the radius is right"
                } else {
                    try {
                        mCircle?.radius = radius.toDouble()
                        toast("Set circle radius: ${mCircle?.radius}")
                    } catch (e: IllegalArgumentException) {
                        toast("IllegalArgumentException ")
                    }
                }
            }
        }
    }

    private var flag: Boolean = true
    private fun setStokeColor() {
        if (flag) {
            mCircle?.strokeColor = Color.RED
            toast("set Circle stroke Color: ${mCircle?.strokeColor}")
            flag = false
        } else {
            mCircle?.strokeColor = Color.GRAY
            toast("set Circle stroke Color: ${mCircle?.strokeColor}")
            flag = true
        }
    }
    private fun setWidth() {
        mCircle?.let {
            val width = circleStokeWidth.text.toString().trim()
            if (checkIsEdit(width)) {
                circleStokeWidth.error = "Please make sure the width is Edited"
            } else {
                if (!checkIsRight(width)) {
                    circleStokeWidth.error = "Please make sure the width is right"
                } else {
                    try {
                        mCircle?.strokeWidth = width.toFloat()
                        toast("set Circle stroke Width: ${mCircle?.strokeWidth}")
                    } catch (e: IllegalArgumentException) {
                        Log.e(TAG, "IllegalArgumentException $e")
                        toast("IllegalArgumentException")
                    }
                }
            }
        }
    }

    private fun setTag() {
        mCircle?.let {
            val tag = circleTag.text.toString().trim()
            if (checkIsEdit(tag)) {
                circleTag.error = "Please make sure the width is Edited"
            } else {
                mCircle?.setTag(tag)
                toast("set Circle Tag: $tag")
            }
        }
    }

    private fun addClickEvent() {
        mCircle?.let {
            hMap?.setOnCircleClickListener { circle ->
                if (mCircle == circle) {
                    toast("Circle is clicked.")
                }
            }
        }
    }
}