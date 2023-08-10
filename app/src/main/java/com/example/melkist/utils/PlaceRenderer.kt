package com.example.melkist.utils

import android.content.Context
import com.example.melkist.models.LocationData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class PlaceRenderer(
    val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<LocationData>
) : DefaultClusterRenderer<LocationData>(context, map, clusterManager){
    override fun onBeforeClusterItemRendered(
        item: LocationData,
        markerOptions: MarkerOptions
    ) {
        markerOptions.icon(item.getLocColor(context = context))
            .position(item.position)
    }
    override fun onClusterItemRendered(clusterItem: LocationData, marker: Marker) {
        marker.tag = clusterItem
    }
}