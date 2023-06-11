package com.example.melkist.views.map

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.data.UserDataStore
import com.example.melkist.databinding.FragMapP1Binding
import com.example.melkist.models.LocationData
import com.example.melkist.models.LocationResponse
import com.example.melkist.utils.PlaceRenderer
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.MapViewModel
import com.example.melkist.views.map.dialog.BottomSheetMapDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import kotlinx.coroutines.launch

class MapP1Frag : Fragment() {
    private var _binding: FragMapP1Binding? = null
    private val binding get() = _binding!!
    private var interaction: Interaction? = null
    private lateinit var mapFragment: SupportMapFragment
    private val viewModel: MapViewModel by activityViewModels()
    private lateinit var userDataStore: UserDataStore
    private lateinit var googleMap: GoogleMap
    fun getGoogleMap () = googleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragMapP1Binding.inflate(inflater, container, false)
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@MapP1Frag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            mapFragment = childFragmentManager.findFragmentById(
                R.id.map_fragment
            ) as SupportMapFragment

            listenToFileList()
            binding.ibtnFilter.setOnClickListener{
                onResume()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun addClusteredMarkers(googleMap: GoogleMap, files: List<LocationData>) {
        val clusterManager = ClusterManager<LocationData>(requireContext(), googleMap)
        clusterManager.renderer = PlaceRenderer(
            requireContext(),
            googleMap,
            clusterManager
        )
        //clusterManager.markerCollection.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))
        clusterManager.addItems(files)
        clusterManager.cluster()
        googleMap.setOnCameraIdleListener {
            clusterManager.onCameraIdle()
        }
        clusterManager.setOnClusterItemClickListener { file ->
            val bottomSheetDialog = BottomSheetMapDialog()
            bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
            UserDataStore(requireContext()).preferenceFlow.asLiveData().observe(viewLifecycleOwner){ user ->
                viewModel.getFileInfoById(user.token!!, file.id!!)
            }
            false
        }
    }

    override fun onResume() {
        super.onResume()
        interaction?.changBottomNavViewVisibility(View.VISIBLE)
        if (!this::userDataStore.isInitialized)
            userDataStore = UserDataStore(requireContext())
        UserDataStore(requireContext()).preferenceFlow.asLiveData().observe(viewLifecycleOwner){
            viewModel.getFiles(it.token!!, it.cityId!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listenToFileList() {
        viewModel.locationResponse.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> {
                    onTrueResult(response)
                }
                false -> {
                    onFalseResult()
                }
                else -> {
                    showToast(requireContext(), resources.getString(R.string.somthing_goes_wrong))
/*                    Log.e(
                        "TAG",
                        "listenToCheckVerificationResult: ${resources.getString(R.string.somthing_goes_wrong)}",
                     )*/
                }
            }
        }
    }

    private fun onFalseResult() {
        // TODO
    }

    private fun onTrueResult(response: LocationResponse){
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                readyMap(response)
            }
        }
    }

    private suspend fun readyMap(response: LocationResponse) {
        googleMap = mapFragment.awaitMap()
        // Wait for map to finish loading
        googleMap.awaitMapLoad()
        // Ensure all places are visible in the map
        val bounds = LatLngBounds.builder()
        //val locList = mutableListOf<Loc>()
        response.data.forEach {
            //it.locations!!.forEach { loc ->
            //    bounds.include(LatLng(loc.lat!!, loc.lng!!))
            //    locList.add(loc)
            //}// if we wanted to show all regions I should use it
            bounds.include(it.position)
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))
        addClusteredMarkers(googleMap, response.data)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Interaction) {
            interaction =
                context as Interaction
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFragmentInteractionListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        interaction = null
    }

    /************************ binding methods *********************************/
    fun showStatusTitle(): String {
        return when (viewModel.getItemType()) {
            MapViewModel.ItemType.SHOW_OWNER -> resources.getString(R.string.show_owner_files)
            MapViewModel.ItemType.SHOW_SEEKER -> resources.getString(R.string.show_seeker_files)
            else -> resources.getString(R.string.show_all_files)
        }
    }

    fun onChangeItemType() {
        // TODO
    }

    fun onSatelliteClicked(googleMap: GoogleMap) {
        if (googleMap.mapType == GoogleMap.MAP_TYPE_NORMAL)
            googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        else
            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    }

    fun onFilterClicked() {
        findNavController().navigate(R.id.action_navigation_home_to_mapP2FilterFilesFrag)
        interaction?.changBottomNavViewVisibility(View.GONE)
    }

    interface Interaction {
        fun changBottomNavViewVisibility(visibility: Int)
    }
}