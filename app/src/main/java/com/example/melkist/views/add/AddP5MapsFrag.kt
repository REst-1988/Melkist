package com.example.melkist.views.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.melkist.AddActivity
import com.example.melkist.R
import com.example.melkist.databinding.FragAddP5MapsBinding
import com.example.melkist.utils.handleSystemException
import com.example.melkist.viewmodels.AddItemViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AddP5MapsFrag : Fragment() {

    lateinit var binding: FragAddP5MapsBinding
    private val viewModel: AddItemViewModel by activityViewModels()
    private lateinit var googleMap: GoogleMap
    private var position: LatLng = LatLng(29.59, 52.58)

    private val callback = OnMapReadyCallback { googleMap ->
        val shiraz = position
        this.googleMap = googleMap
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shiraz, 12.0f))

        if (viewModel.lat != null && viewModel.lng != null) {
            position = LatLng(viewModel.lat!!, viewModel.lng!!)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 14.0f))
        } else if (viewModel.regionId != 0 && viewModel.regionLat != null && viewModel.regionLng != null) {
            position = LatLng(viewModel.regionLat!!, viewModel.regionLng!!)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 14.0f))
        }
        googleMap.setOnCameraIdleListener {
            position = googleMap.cameraPosition.target
            Log.d("MapActivity", "Position: $position")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragAddP5MapsBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@AddP5MapsFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
        } catch (e: Exception) {
            handleSystemException(lifecycleScope, "${(activity as AddActivity).user?.id}, AddP5MapsFrag, onViewCreated,", e)
        }
    }

    /************** binding commands **********************/
    fun back() {
        findNavController().navigate(
            R.id.action_addP5MapsFrag_to_addP4LocationFrag
        )
    }

    fun onChoose() {
        viewModel.lat = position.latitude
        viewModel.lng = position.longitude
        googleMap.addMarker(MarkerOptions().position(position))
        googleMap.snapshot { imageBitmap ->
            imageBitmap?.apply {
                viewModel.setMapSnapShot(this)
            }
        }
        back()
    }
}