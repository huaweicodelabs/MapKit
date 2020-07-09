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

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.SupportMapFragment
import com.huawei.hms.maps.model.*
import com.huawei.mapkitsample.utils.NetworkRequestManager
import com.huawei.mapkitsample.utils.Utill.toast
import kotlinx.android.synthetic.main.activity_route_planning_demo.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/* Route Planning*/
class RoutePlanningDemoActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mSupportMapFragment: SupportMapFragment
    private var hMap: HuaweiMap? = null
    private var mMarkerOrigin: Marker? = null
    private var mMarkerDestination: Marker? = null
    private var latLng1 = LatLng(54.216608, -4.66529)
    private var latLng2 = LatLng(54.209673, -4.64002)
    private val mPolyline: MutableList<Polyline> =
        ArrayList()
    private val mPaths: MutableList<List<LatLng>> =
        ArrayList()
    private var mLatLngBounds: LatLngBounds? = null

    companion object {
        private const val TAG = "RoutePlanning"
    }

    private val mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                0 -> renderRoute(mPaths, mLatLngBounds)
                1 -> {
                    val bundle = msg.data
                    val errorMsg = bundle.getString("errorMsg")
                    toast(errorMsg.toString())
                }
                else -> {
                    Log.v("Default Msg- ",msg.what.toString())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_planning_demo)
        val fragment =
            supportFragmentManager.findFragmentById(R.id.mapfragment_routeplanningdemo)
        if (fragment is SupportMapFragment) {
            mSupportMapFragment = fragment
            mSupportMapFragment.getMapAsync(this)
        }
        btnGetWalkingRouteResult.setOnClickListener {
            getWalkingRouteResult()
        }
        btnGetBicyclingRouteResult.setOnClickListener {
            getBicyclingRouteResult()
        }
        btnGetDrivingRouteResult.setOnClickListener {
            getDrivingRouteResult()
        }
        btnSetOrigin.setOnClickListener {
            setOrigin()
        }
        btnSetDestination.setOnClickListener {
            setDestination()
        }
    }

    override fun onMapReady(huaweiMap: HuaweiMap) {
        hMap = huaweiMap
        hMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 13f))
        addOriginMarker(latLng1)
        addDestinationMarker(latLng2)
    }

    fun getWalkingRouteResult() {
        removePolyline()
        NetworkRequestManager.getWalkingRoutePlanningResult(latLng1, latLng2,
            object : NetworkRequestManager.Companion.OnNetworkListener {
                override fun requestSuccess(result: String?) {
                    generateRoute(result!!)
                }

                override fun requestFail(errorMsg: String?) {
                    val msg = Message.obtain()
                    val bundle = Bundle()
                    bundle.putString("errorMsg", errorMsg)
                    msg.what = 1
                    msg.data = bundle
                    mHandler.sendMessage(msg)
                }
            })
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun getBicyclingRouteResult() {
        removePolyline()
        NetworkRequestManager.getBicyclingRoutePlanningResult(latLng1, latLng2,
            object : NetworkRequestManager.Companion.OnNetworkListener {
                override fun requestSuccess(result: String?) {
                    generateRoute(result!!)
                }

                override fun requestFail(errorMsg: String?) {
                    val msg = Message.obtain()
                    val bundle = Bundle()
                    bundle.putString("errorMsg", errorMsg)
                    Log.d("sfj", errorMsg)
                    msg.what = 1
                    msg.data = bundle
                    mHandler.sendMessage(msg)
                }
            })
    }

    fun getDrivingRouteResult() {
        removePolyline()
        NetworkRequestManager.getDrivingRoutePlanningResult(latLng1, latLng2,
            object : NetworkRequestManager.Companion.OnNetworkListener {
                override fun requestSuccess(result: String?) {
                    generateRoute(result!!)
                }

                override fun requestFail(errorMsg: String?) {
                    val msg = Message.obtain()
                    val bundle = Bundle()
                    bundle.putString("errorMsg", errorMsg)
                    msg.what = 1
                    msg.data = bundle
                    mHandler.sendMessage(msg)
                }
            })
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun generateRoute(json: String) {
        try {
            val jsonObject = JSONObject(json)
            val routes = jsonObject.optJSONArray("routes")
            if (null == routes || routes.length() == 0) {
                return
            }
            val route = routes.getJSONObject(0)
            // get route bounds
            val bounds = route.optJSONObject("bounds")
            if (null != bounds && bounds.has("southwest") && bounds.has("northeast")) {
                val southwest = bounds.optJSONObject("southwest")
                val northeast = bounds.optJSONObject("northeast")
                val sw = LatLng(southwest!!.optDouble("lat"), southwest.optDouble("lng"))
                val ne = LatLng(northeast!!.optDouble("lat"), northeast.optDouble("lng"))
                mLatLngBounds = LatLngBounds(sw, ne)
            }
            // get paths
            val paths = route.optJSONArray("paths")
            for (i in 0 until paths!!.length()) {
                val path = paths.optJSONObject(i)
                val mPath: MutableList<LatLng> = ArrayList()
                val steps = path.optJSONArray("steps")
                for (j in 0 until steps!!.length()) {
                    val step = steps.optJSONObject(j)
                    val polyline = step.optJSONArray("polyline")
                    for (k in 0 until polyline.length()) {
                        if (j > 0 && k == 0) {
                            continue
                        }
                        val line = polyline.getJSONObject(k)
                        val lat = line.optDouble("lat")
                        val lng = line.optDouble("lng")
                        val latLng = LatLng(lat, lng)
                        mPath.add(latLng)
                    }
                }
                mPaths.add(i, mPath)
            }
            mHandler.sendEmptyMessage(0)
        } catch (e: JSONException) {
            Log.e(
                TAG,
                "JSONException$e"
            )
        }
    }

    /**
     * Render the route planning result
     *
     * @param paths
     * @param latLngBounds
     */
    private fun renderRoute(
        paths: List<List<LatLng>>?,
        latLngBounds: LatLngBounds?
    ) {
        if (null == paths || paths.isEmpty() || paths[0].isEmpty()) {
            return
        }
        for (i in paths.indices) {
            val path = paths[i]
            val options =
                PolylineOptions().color(Color.BLUE).width(5f)
            for (latLng in path) {
                options.add(latLng)
            }
            val polyline = hMap!!.addPolyline(options)
            mPolyline.add(i, polyline)
        }
        addOriginMarker(paths[0][0])
        addDestinationMarker(paths[0][paths[0].size - 1])
        if (null != latLngBounds) {
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, 5)
            hMap?.moveCamera(cameraUpdate)
        } else {
            hMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(paths[0][0], 13f))
        }
    }

    fun setOrigin() {
        if (!TextUtils.isEmpty(edtOriginLat.text) && !TextUtils.isEmpty(edtOriginLng.text)) {
            latLng1 =
                LatLng(
                    java.lang.Double.valueOf(edtOriginLat.text.toString().trim()),
                    java.lang.Double.valueOf(edtOriginLng.text.toString().trim())
                )
            removePolyline()
            addOriginMarker(latLng1)
            hMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 13f))
            mMarkerOrigin?.showInfoWindow()
        }
    }

    fun setDestination() {
        if (!TextUtils.isEmpty(edtDestinationLat.text) && !TextUtils.isEmpty(
                edtDestinationLng.text
            )
        ) {
            latLng2 =
                LatLng(
                    edtDestinationLat.text.toString().trim().toDouble(),
                    edtDestinationLng.text.toString().trim().toDouble()
                )
            removePolyline()
            addDestinationMarker(latLng2)
            hMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 13f))
            mMarkerDestination?.showInfoWindow()
        }
    }

    @Suppress("DEPRECATION")
    private fun addOriginMarker(latLng: LatLng) {
            mMarkerOrigin?.let {
                mMarkerOrigin?.remove()
            }
            mMarkerOrigin = hMap?.addMarker(
            MarkerOptions().position(latLng)
                .anchor(0.5f, 0.9f) // .anchorMarker(0.5f, 0.9f)
                .title("Origin")
                .snippet(latLng.toString())
        )
    }

    @Suppress("DEPRECATION")
    private fun addDestinationMarker(latLng: LatLng) {
            mMarkerDestination?.let {
                mMarkerDestination?.remove()
            }
        mMarkerDestination = hMap?.addMarker(
            MarkerOptions().position(latLng).anchor(
                0.5f,
                0.9f
            ).title("Destination").snippet(latLng.toString())
        )
    }

    private fun removePolyline() {
        for (polyline in mPolyline) {
            polyline.remove()
        }
        mPolyline.clear()
        mPaths.clear()
        mLatLngBounds = null
    }
}