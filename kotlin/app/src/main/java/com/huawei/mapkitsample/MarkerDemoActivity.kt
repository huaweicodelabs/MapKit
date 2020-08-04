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
 *  2020.1.3-Changed modify the import classes type and add some marker demos.
 *                  Huawei Technologies Co., Ltd.
 *
 */

package com.huawei.mapkitsample

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.HuaweiMap.InfoWindowAdapter
import com.huawei.hms.maps.HuaweiMap.OnMarkerDragListener
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.SupportMapFragment
import com.huawei.hms.maps.model.*
import com.huawei.mapkitsample.utils.Utill.toast
import kotlinx.android.synthetic.main.activity_marker_demo.*
import kotlinx.android.synthetic.main.custom_info_contents.*
import java.util.*

/**
 * Marker related
 */
@Suppress("DEPRECATION")
class MarkerDemoActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mSupportMapFragment: SupportMapFragment
    private var hMap: HuaweiMap? = null
    private var mOrsay: Marker? = null
    private var mParis: Marker? = null
    private var mSerris: Marker? = null
    private var mWindowType = 0
    private var markerList: MutableList<Marker>? = ArrayList()

    companion object {
        private const val TAG = "MarkerDemoActivity"
        private val PARIS = LatLng(48.893478, 2.334595)
        private val SERRIS = LatLng(48.7, 2.31)
        private val ORSAY = LatLng(48.85, 2.78)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marker_demo)
        val fragment =
                supportFragmentManager.findFragmentById(R.id.mapfragment_markerdemo)
        if (fragment is SupportMapFragment) {
            mSupportMapFragment = fragment
            mSupportMapFragment.getMapAsync(this)
        }
        /**
         * Add a marker to the map
         */
        btnAddMarker.setOnClickListener {
            addMarker()
        }
        /**
         * Remove the marker from the map
         */
        btnDeleteMarker.setOnClickListener {
            deleteMarker()
        }
        /**
         * Set the title attribute of the marker
         *
         */
        btnSetTitle.setOnClickListener {
            setTitle()
        }
        /**
         * Set the snippet attribute of the marker
         *
         */
        btnSetSnippet.setOnClickListener {
            val snippetStr = edtSnippet.text.toString()
            if (mOrsay != null && "" != snippetStr) {
                mOrsay?.snippet = snippetStr
            }
        }
        btnDefaultWindow.setOnClickListener {
            mWindowType = 1
            toast("set mWindowType 1")

        }
        btnContentWindow.setOnClickListener {
            mWindowType = 2
            toast("set mWindowType 2")
        }
        btnCustomWindow.setOnClickListener {
            mWindowType = 3
            toast("set mWindowType 3")
        }
        /**
         * Set the marker to drag
         */
        btnDragMarker.setOnClickListener {
            mSerris?.let {
                mSerris?.isDraggable = true
                toast("Draggable True")
            }
        }
        /**
         * Set the icon attribute of the marker
         */
        btnSetMarkerIcon.setOnClickListener {
            setMarkerIcon()
        }
        /**
         * Set the tag attribute of the marker
         */
        btnTagTest.setOnClickListener {
            val tagStr = edtTag.text.toString()
            if (mParis != null && "" != tagStr) {
                mParis?.tag = tagStr
            }
        }
        /**
         * Set the anchor attribute of the marker
         */
        btnSetMarkerAnchor.setOnClickListener {
            mParis?.let {
                mParis?.setAnchor(0.9f, 0.9f)
            }
        }
        /**
         * Hide the information window of the marker
         */
        btnHideWindow.setOnClickListener {
            mParis?.let {
                mParis?.hideInfoWindow()
            }
        }
        /**
         * Show the information window of the marker
         */
        btnShowInfoWindow.setOnClickListener {
            mParis?.let {
                mParis?.showInfoWindow()
            }
        }

        /**
         * Repositions the camera according to the instructions defined in the update
         */
        btnSetCamera.setOnClickListener {
            setCamera()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(paramHuaweiMap: HuaweiMap) {
        Log.i(TAG, "onMapReady: ")
        hMap = paramHuaweiMap
        hMap?.apply {
            isMyLocationEnabled = true
            setInfoWindowAdapter(CustomInfoWindowAdapter())
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(48.893478, 2.334595), 14f))
        }

    }

    @SuppressLint("InflateParams")
    internal inner class CustomInfoWindowAdapter : InfoWindowAdapter {
        private val mWindowView: View
        private val mContentsView: View
        override fun getInfoWindow(marker: Marker): View {
            val view: View? = null
            Log.d(TAG, "getInfoWindow")
            if (mWindowType != 3) {
                return view!!
            }
            render(marker, mWindowView)
            return mWindowView
        }

        override fun getInfoContents(marker: Marker): View {
            val view: View? = null
            Log.d(TAG, "getInfoContents")
            if (mWindowType != 2) {
                return view!!
            }
            render(marker, mContentsView)
            return mContentsView
        }

        private fun render(marker: Marker, view: View) {
            setMarkerBadge(marker)
            setMarkerTextView(marker)
            setMarkerSnippet(marker, view)
        }

        private fun setMarkerBadge(marker: Marker) {
            // Use the equals method to determine if the marker is the same ,do not use"=="
            val markerBadge: Int = when (marker) {
                mParis -> {
                    R.drawable.badge_bj
                }
                mOrsay -> {
                    R.drawable.badge_sh
                }
                mSerris -> {
                    R.drawable.badge_nj
                }
                else -> {
                    0
                }
            }
            imgv_badge.setImageResource(
                    markerBadge
            )
        }

        private fun setMarkerTextView(marker: Marker) {
            val markerTitle = marker.title
            var titleView: TextView? = null
            val mobject: Any = txtv_titlee
            if (mobject is TextView) {
                titleView = mobject
            }
            if (markerTitle == null) {
                titleView?.text = ""
            } else {
                val titleText = SpannableString(markerTitle)
                titleText.setSpan(
                        ForegroundColorSpan(Color.BLUE),
                        0,
                        titleText.length,
                        0
                )
                titleView?.text = titleText
            }
        }

        private fun setMarkerSnippet(marker: Marker, view: View) {
            var markerSnippet = marker.snippet
            marker.tag?.let {
                markerSnippet = marker.tag as String
            }
            val snippetView =
                    view.findViewById<View>(R.id.txtv_snippett) as TextView
            if (markerSnippet != null && markerSnippet.isNotEmpty()) {
                val snippetText = SpannableString(markerSnippet)
                snippetText.setSpan(
                        ForegroundColorSpan(Color.RED),
                        0,
                        markerSnippet.length,
                        0
                )
                snippetView.text = snippetText
            } else {
                snippetView.text = ""
            }
        }

        init {
            mWindowView = layoutInflater.inflate(R.layout.custom_info_window, null)
            mContentsView = layoutInflater.inflate(R.layout.custom_info_contents, null)
        }
    }

    private fun addMarker() {
        if (null == hMap) {
            return
        }
        if (mParis == null && mOrsay == null && mSerris == null) { // Uses a colored icon.
            mParis = hMap?.addMarker(
                    MarkerOptions().position(PARIS).title("paris").snippet(
                            "hello"
                    ).clusterable(true)
            )
            mOrsay = hMap?.addMarker(
                    MarkerOptions().position(ORSAY)
                            .alpha(0.5f)
                            .title("Orsay")
                            .snippet("hello")
                            .clusterable(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.badge_ph))
            )
            mSerris = hMap?.addMarker(
                    MarkerOptions().position(SERRIS)
                            .title("Serris")
                            .snippet("Can be dragged after DragMarker.")
                            .clusterable(true)
            )
            hMap?.setOnMarkerClickListener { marker ->
                val clusterable = marker.isClusterable
                toast(clusterable.toString())
                true
            }
        }
        // Add a marker when the point is long clicked.
        hMap?.setOnMapLongClickListener { latLng ->
            Log.d(TAG, "Map is long clicked.")
            val mMarker =
                    hMap!!.addMarker(MarkerOptions().position(latLng).title("I am Marker!"))
            markerList!!.add(mMarker)
            Log.d(
                    TAG,
                    "markerList size is." + markerList?.size
            )
        }
        addMarkerListener()
    }

    /**
     * Set the listener associated with the marker
     */
    private fun addMarkerListener() {
        hMap?.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {
                Log.i(TAG, "onMarkerDragStart: ")
            }

            override fun onMarkerDrag(marker: Marker) {
                Log.i(TAG, "onMarkerDrag: ")
            }

            override fun onMarkerDragEnd(marker: Marker) {
                Log.i(TAG, "onMarkerDragEnd: ")
            }
        })
        hMap?.setOnInfoWindowClickListener { marker ->
            if (marker == mSerris) {
                toast("mMelbourne info window is clicked")
            }
            if (marker == mOrsay) {
                toast("mSydney info window is clicked")
            }
            if (marker == mParis) {
                toast("mBrisbane info window is clicked")
            }
        }
        hMap?.setOnInfoWindowCloseListener {
            toast("info window close")
        }
        hMap?.setOnInfoWindowLongClickListener {
            toast("onInfo Window LongClick")
        }
    }

    fun deleteMarker() {
        mSerris?.let {
            mSerris?.remove()
            mSerris = null
        }
        mOrsay?.let {
            mOrsay?.remove()
            mOrsay = null
        }
        mParis?.let {
            mParis?.remove()
            mParis = null
        }
        // remove the markers added by long click.
        if (null != markerList && markerList!!.size > 0) {
            for (iMarker in markerList!!) {
                iMarker.remove()
                //  iMarker = null
            }
            markerList?.clear()
        }
    }

    fun setMarkerIcon() {
        mOrsay?.let {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.badge_tr)
            val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap)
            mOrsay?.setIcon(bitmapDescriptor)
        }
    }

    fun setCamera() {
        try {
            var latLng: LatLng? = null
            var zoom = 0f
            var bearing = 0f
            var tilt = 0f
            if (edtCameraLng.text.isNotEmpty() && edtCameraLat.text.isNotEmpty()) {
                latLng =
                        LatLng(
                                (edtCameraLat.text.toString()).toDouble(),
                                (edtCameraLng.text.toString()).toDouble()
                        )
            }
            if (edtCameraZoom.text.isNotEmpty()) {
                zoom =
                        (edtCameraZoom.text.toString()).toFloat()
            }
            if (edtCameraBearing.text.isNotEmpty()) {
                bearing =
                        (edtCameraBearing.text.toString()).toFloat()
            }
            if (edtCameraTilt.text.isNotEmpty()) {
                tilt =
                        (edtCameraTilt.text.toString()).toFloat()
            }
            val cameraPosition =
                    CameraPosition.builder().target(latLng).zoom(zoom).bearing(bearing).tilt(tilt)
                            .build()
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            hMap?.moveCamera(cameraUpdate)
        } catch (e: IllegalArgumentException) {
            Log.e(
                    TAG,
                    "IllegalArgumentException $e"
            )
            toast("IllegalArgumentException")
        } catch (e: NullPointerException) {
            Log.e(
                    TAG,
                    "NullPointerException $e"
            )
            toast("NullPointerException")
        }
    }

    fun setTitle() {
        val titleStr = edtTitle.text.toString()
        if (mParis != null && "" != titleStr) {
            mParis?.title = titleStr
            toast("BJ title is " + mParis?.title)
        }
    }
}