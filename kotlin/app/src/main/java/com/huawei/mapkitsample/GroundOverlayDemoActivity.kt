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

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.SupportMapFragment
import com.huawei.hms.maps.model.*
import com.huawei.mapkitsample.R.string.filePathText
import com.huawei.mapkitsample.utils.CheckUtils.checkIsEdit
import com.huawei.mapkitsample.utils.CheckUtils.checkIsRight
import com.huawei.mapkitsample.utils.MapUtils
import com.huawei.mapkitsample.utils.Utill.toast
import kotlinx.android.synthetic.main.activity_circle_demo.*
import kotlinx.android.synthetic.main.activity_groudoverlay_demo.*
import kotlinx.android.synthetic.main.activity_groudoverlay_demo.btnGetTag
import kotlinx.android.synthetic.main.activity_groudoverlay_demo.btnSetTag
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * about groundOverlay
 */
class GroundOverlayDemoActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mSupportMapFragment: SupportMapFragment
    private var hMap: HuaweiMap? = null
    private var overlay: GroundOverlay? = null


    companion object {
        private const val TAG = "GroundOverlayDemoActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groudoverlay_demo)
        mSupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapInGroundOverlay) as SupportMapFragment
        mSupportMapFragment.getMapAsync(this)

        /**
         * Create a GroundOverlay using the images in the assets directory
         */
        btnAddFromAsset.setOnClickListener {
            addFromAsset()
        }
        /**
         * Create a GroundOverlay using the resources of the bitmap image
         */
        btnAddFromResource.setOnClickListener {
            addFromResource()
        }
        /**
         * Create GroundOverlay
         */
        btnAddFromBitmap.setOnClickListener {
            addFromBitmap()
        }

        /**
         * Create GroundOverlay
         */
        btnAddFromFile.setOnClickListener {
            addFromFile()
        }

        /**
         * Create GroundOverlay
         */
        btnAddFromPath.setOnClickListener {
            addFromPath()
        }
        /**
         * Remove the Groudoverlay
         */
        btnRemoveGroundOverlay.setOnClickListener {
            Log.d(TAG, "removeGroudoverlay: ")
                overlay?.let {overlay?.remove()  }
        }
        /**
         * Set the scope of GroundOverlay
         */
        btnSetPointsBy2Points.setOnClickListener {
            setPointsBy2Points()
        }
        /**
         * Get the scope of GroundOverlay
         */
        btnGetPointsBy2Points.setOnClickListener {
            getPointsBy2Points()
        }
        /**
         * Set the height and width of the GroundOverlay
         */
        btnSetPointsBy1PointsWidthHeight.setOnClickListener {
            setPointsBy1PointsWidthHeight()
        }
        /**
         * Set the height, width, and position of the GroundOverlay
         */
        btnGetPointsBy1PointsWidthHeight.setOnClickListener {
            getPointsBy1PointsWidthHeight()
        }

        /**
         * Get the properties of the GroundOverlay
         */
        btnGetAttributes.setOnClickListener {
            getAttributes()
        }
        /**
         * Change the image of GroundOverlay
         */
        btnSetImage.setOnClickListener {
            overlay?.setImage(BitmapDescriptorFactory.fromResource(R.drawable.makalong))
        }
        /**
         * Set the tag of GroundOverlay
         */
        btnSetTag.setOnClickListener {
          setTag()
        }
        /**
         * Get the tag of GroundOverlay
         */
        btnGetTag.setOnClickListener {
                val str="${getString(R.string.overlaytagisText)}${overlay?.tag}}"           //error
                circleShown.text = str
        }
        /**
         * Set GroundOverlay visible
         */
        btnSetVisibleTrue.setOnClickListener {
                overlay?.isVisible = true               //error toast msg
        }
        /**
         * Setting GroundOverlay is not visible
         */
        btnSetVisibleFalse.setOnClickListener {
                overlay?.isVisible = false                 // error toast msg
        }
    }

    override fun onMapReady(paramHuaweiMap: HuaweiMap) {
        Log.i(TAG, "onMapReady: ")
        hMap = paramHuaweiMap
        hMap?.apply {
            isMyLocationEnabled = true
            uiSettings?.isZoomControlsEnabled = false
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(48.893478, 2.334595), 10f))
        }

    }

    /**
     * Create a GroundOverlay using the images in the assets directory
     */
    private fun addFromAsset() {
        if (hMap == null) {
            return
        }
        overlay?.remove()
        Log.d(TAG, "addFromAsset: ")
        val options =
            GroundOverlayOptions().position(MapUtils.FRANCE2, 50f, 50f)
                .image(BitmapDescriptorFactory.fromAsset("images/niuyouguo.jpg"))
        overlay = hMap?.addGroundOverlay(options)
        val cameraPosition =
            CameraPosition.builder().target(MapUtils.FRANCE2).zoom(18f).bearing(0f).tilt(0f).build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        hMap?.moveCamera(cameraUpdate)
    }


    private  fun addFromResource() {
        if (hMap == null) {
            return
        }
        overlay?.remove()
        Log.d(TAG, "addFromResource: ")
        val options =
            GroundOverlayOptions().position(MapUtils.FRANCE2, 50f, 50f)
                .image(BitmapDescriptorFactory.fromResource(R.drawable.niuyouguo))
        overlay = hMap?.addGroundOverlay(options)
        val cameraPosition =
            CameraPosition.builder().target(MapUtils.FRANCE2).zoom(18f).bearing(0f).tilt(0f).build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        hMap?.moveCamera(cameraUpdate)
    }

    private fun addFromBitmap() {
        if (hMap == null) {
            return
        }
        overlay?.remove()
        Log.d(TAG, "addFromBitmap: ")
        val vectorDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.niuyouguo, null)
        val bitmap = Bitmap.createBitmap(
            vectorDrawable!!.intrinsicWidth, vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        val options =
            GroundOverlayOptions().position(MapUtils.FRANCE2, 50f, 50f)
                .image(BitmapDescriptorFactory.fromBitmap(bitmap))
        overlay = hMap?.addGroundOverlay(options)
        val cameraPosition =
            CameraPosition.builder().target(MapUtils.FRANCE2).zoom(18f).bearing(0f).tilt(0f).build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        hMap?.moveCamera(cameraUpdate)
    }
    private fun addFromFile() {
        if (hMap == null) {
            return
        }
        overlay?.remove()
        Log.d(TAG, "addFromFile: ")
        var out: FileOutputStream? = null
        val fileName = "maomao.jpg"
        val localFile = filesDir.toString() + File.separator + fileName
        try {
            val bitmap =
                BitmapFactory.decodeStream(assets.open("images/niuyouguo.jpg"))
            out = FileOutputStream(File(localFile))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        } catch (e: FileNotFoundException) {
            Log.d(
                TAG,
                "addFromFile FileNotFoundException: $e"
            )
        } catch (e: IOException) {
            Log.d(
                TAG,
                "addFromFile IOException: $e"
            )
        } finally {
            try {
                    out?.let { out.flush()
                        out.close() }

            } catch (e: IOException) {
                Log.d(
                    TAG,
                    "addFromFile close fileOutputStream IOException: $e"
                )
            }
        }
        val options =
            GroundOverlayOptions().position(MapUtils.FRANCE2, 30f, 60f)
                .image(BitmapDescriptorFactory.fromFile(fileName))
        overlay = hMap?.addGroundOverlay(options)
        val cameraPosition =
            CameraPosition.builder().target(MapUtils.FRANCE2).zoom(18f).bearing(0f).tilt(0f).build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        hMap?.moveCamera(cameraUpdate)
    }

    private fun addFromPath() {
        if (hMap == null) {
            return
        }
        Log.d(TAG, "addFromPath")
        overlay?.remove()
        val path = getString(filePathText)
        var out: FileOutputStream? = null
        try {
            val bitmap =
                BitmapFactory.decodeStream(assets.open("images/niuyouguo.jpg"))
            out = FileOutputStream(File(path))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        } catch (e: Exception) {
            Log.d(
                TAG,
                "addFromFile FileNotFoundException: $e"
            )
        } finally {
            try {
                out?.let {
                    it.flush()
                    it.close()
                }
            } catch (e: IOException) {
                Log.d(
                    TAG,
                    "addFromFile close fileOutputStream IOException: $e"
                )
            }
        }
        val options =
            GroundOverlayOptions().position(MapUtils.FRANCE2, 30f, 60f)
                .image(BitmapDescriptorFactory.fromPath(path))
        val hmapOverlay = hMap?.addGroundOverlay(options) ?: return
        this.overlay = hmapOverlay
        val cameraPosition =
            CameraPosition.builder().target(MapUtils.FRANCE2).zoom(18f).bearing(0f).tilt(0f).build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        hMap?.moveCamera(cameraUpdate)
    }


    private fun getAttributes() {
            overlay?.let {
                val bounds = if (overlay?.bounds == null) {
                    "null"
                } else {
                    overlay?.bounds.toString()
                }
                val position = if (overlay?.position == null) {
                    "null"
                } else {
                    overlay?.position.toString()
                }
                toast("position: $position  $ width: ${overlay?.width} $ height: ${overlay?.height} $ bounds: $bounds")
            }
    }


    private fun setPointsBy2Points() {
            val northeastLatitude =
                toprightLatitude?.text.toString().trim()
            val northeastLongitude =
                toprightLongtitude?.text.toString().trim()
            val southwestLatitude =
                bottomleftLatitude?.text.toString().trim()
            val southwestLongitude =
                bottomleftLongtitude?.text.toString().trim()
            if (checkIsEdit(northeastLatitude) || checkIsEdit(northeastLongitude) || checkIsEdit(
                    southwestLatitude
                )
                || checkIsEdit(southwestLongitude)
            ) {
                toast("Please make sure these latlng are Edited")
            } else {
                if (!checkIsRight(northeastLatitude) || !checkIsRight(northeastLongitude)
                    || !checkIsRight(southwestLatitude) || !checkIsRight(southwestLongitude)
                ) {
                    toast("Please make sure these latlng are right")
                } else {
                    try {
                        overlay?.setPositionFromBounds(
                            LatLngBounds(
                                LatLng(
                                    java.lang.Double.valueOf(southwestLatitude),
                                    java.lang.Double.valueOf(southwestLongitude)
                                ),
                                LatLng(
                                    java.lang.Double.valueOf(northeastLatitude),
                                    java.lang.Double.valueOf(northeastLongitude)
                                )
                            )
                        )
                        val cameraPosition =
                            CameraPosition.builder().target(overlay?.position).zoom(18f)
                                .bearing(0f).tilt(0f).build()
                        val cameraUpdate =
                            CameraUpdateFactory.newCameraPosition(cameraPosition)
                        hMap?.moveCamera(cameraUpdate)
                    } catch (e: IllegalArgumentException) {
                        toast("IllegalArgumentException ")
                    }
                }
        }
    }

    private fun getPointsBy2Points() {
            if (overlay?.bounds == null) {
                toast("the groundoverlay is added by the other function")
            } else {
                toast("LatlngBounds :" + overlay?.bounds.toString())
            }
    }


    private fun setPointsBy1PointsWidthHeight() {
       overlay?.let {
           val width = imageWidth?.text.toString().trim()
           val height = imageHeight?.text.toString().trim()
           val latitude = positionLatitude?.text.toString().trim()
           val longitude =
               positionLongtitude?.text.toString().trim()
           if (checkIsEdit(width) || checkIsEdit(height) || checkIsEdit(latitude) || checkIsEdit(
                   longitude
               )
           ) {
               toast("Please make sure the width & height & position is Edited")
           } else {
               if (!checkIsRight(width) || !checkIsRight(height) || !checkIsRight(latitude)
                   || !checkIsRight(longitude)
               ) {
                   toast("Please make sure the width & height & position is right")
               } else {
                   val position = LatLng(
                       latitude.toDouble(),
                       longitude.toDouble()
                   )
                   overlay?.position = position
                   overlay?.setDimensions(
                       width.toFloat(),
                       height.toFloat()
                   )
                   val cameraPosition =
                       CameraPosition.builder().target(position).zoom(18f).bearing(0f).tilt(0f)
                           .build()
                   val cameraUpdate =
                       CameraUpdateFactory.newCameraPosition(cameraPosition)
                   hMap?.moveCamera(cameraUpdate)
               }
           }
       }
    }
    private fun getPointsBy1PointsWidthHeight() {
            overlay?.let {
                if (overlay?.position == null || overlay?.height == 0f || overlay?.width == 0f) {
                    toast("the groundoverlay is added by the other function")
                } else {
                    toast("Position :" + overlay?.position.toString() + "With :" + overlay?.width + "Height :" + overlay?.height)
                }
            }
    }

    private fun setTag() {
            overlay?.let {
                val tag = groundOverlayTag?.text.toString().trim()
                if (checkIsEdit(tag)) {
                    toast( "Please make sure the tag is Edited")
                } else {
                    overlay?.tag = tag
                }
            }
    }
}