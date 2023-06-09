package com.example.melkist.views.map

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.databinding.FragMapP1Binding
import com.example.melkist.interfaces.Interaction
import com.example.melkist.models.FileTypes
import com.example.melkist.models.FilterFileData
import com.example.melkist.models.LocationData
import com.example.melkist.models.LocationResponse
import com.example.melkist.utils.DATA
import com.example.melkist.utils.FILTER_RESULT_KEY
import com.example.melkist.utils.PlaceRenderer
import com.example.melkist.utils.TYPE_OPTIONS_TAG
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.isSystemDarkMode
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.MainViewModel
import com.example.melkist.views.universal.dialog.BottomSheetFileDetailDialog
import com.example.melkist.views.universal.dialog.BottomSheetUniversalList
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import com.google.maps.android.ktx.utils.contains
import kotlinx.coroutines.launch


class MapP1Frag : Fragment() {
    private var isPolygonClicked: Boolean = false
    private var _binding: FragMapP1Binding? = null
    private val binding get() = _binding!!
    private var interaction: Interaction? = null
    private lateinit var mapFragment: SupportMapFragment
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var clusterManager: ClusterManager<LocationData>
    private var files: List<LocationData> = listOf()
    private lateinit var googleMap: GoogleMap
    fun getGoogleMap() = googleMap
    private lateinit var polygon: Polygon
    private lateinit var polygonOptions: PolygonOptions
    private val latLngList = arrayListOf<LatLng>()
    private val markerList = arrayListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(FILTER_RESULT_KEY){ _, bundle ->
            val filterfileData = bundle.getSerializable(DATA)!! as FilterFileData
            Log.e("TAG", "onCreate: rooms.from = ${filterfileData.rooms.from}")
            Log.e("TAG", "onCreate: rooms.to = ${filterfileData.rooms.to}")
            Log.e("TAG", "onCreate: age.from = ${filterfileData.age.from}")
            Log.e("TAG", "onCreate: age.from = ${filterfileData.age.to}")
            Log.e("TAG", "onCreate: catId = ${filterfileData.catId}")
            Log.e("TAG", "onCreate: price.from = ${filterfileData.price.from}")
            Log.e("TAG", "onCreate: price.to = ${filterfileData.price.to}")
            Log.e("TAG", "onCreate: regionId = ${filterfileData.regionId}")
            Log.e("TAG", "onCreate: size.from = ${filterfileData.size.from}")
            Log.e("TAG", "onCreate: size.to = ${filterfileData.size.to}")
            Log.e("TAG", "onCreate: subCatId= ${filterfileData.subCatId}")
            Log.e("TAG", "onCreate: typeId = ${filterfileData.typeId}")
            viewModel.resetLocations()
            (activity as MainActivity).user?.apply {
                viewModel.getFilterFiles(
                    token!!,
                    filterfileData
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
/*            binding.ibtnFilter.setOnClickListener {// TODO check why I needed this
                onResume()
            }*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addClusteredMarkers(googleMap: GoogleMap, files: List<LocationData>) {
        if (!::clusterManager.isInitialized) {
            clusterManager = ClusterManager<LocationData>(requireContext(), googleMap)
        }
        clusterManager.renderer = PlaceRenderer(
            requireContext(), googleMap, clusterManager
        )
        clusterManager.clearItems()
        clusterManager.addItems(files)
        clusterManager.cluster()
        googleMap.setOnCameraIdleListener {
            clusterManager.onCameraIdle()
        }
        onClusterMarkerClickOnAndOff(true)
    }

    private fun onClusterMarkerClickOnAndOff (isOn: Boolean) {
        if (isOn)
            clusterManager.setOnClusterItemClickListener { file ->
                if (file.fileTypeId == FileTypes().owner.id) {
                    val bottomSheetDialog = BottomSheetFileDetailDialog(this@MapP1Frag, file.id!!)
                    bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
                }else {
                    Toast.makeText(
                        requireContext(),
                        "فایل خواهان غیر قابل انتخاب می باشد!",
                        Toast.LENGTH_SHORT
                    ).show() // TODO: remove this after test and present
                }
                true
            }
        else
            clusterManager.setOnClusterItemClickListener {
                true
            }
    }

    override fun onResume() {
        super.onResume()
        interaction?.changBottomNavViewVisibility(View.VISIBLE)
        (activity as MainActivity).user?.apply {
            viewModel.getFiles(token!!, cityId!!)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listenToFileList() {
        viewModel.locationResponse.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> onGetMapDataTrueResult(response)
                false -> onGetMapDataFalseResult(response)
                else -> showToast(
                    requireContext(), resources.getString(R.string.somthing_goes_wrong)
                )
            }
        }
    }

    private fun onGetMapDataFalseResult(response: LocationResponse) {
        showDialogWithMessage(requireContext(), concatenateText(response.errors)) { d, _ ->
            d.dismiss()
        }
    }

    private fun onGetMapDataTrueResult(response: LocationResponse) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                readyMap(response)
            }
        }
    }

    private suspend fun readyMap(response: LocationResponse) {
        googleMap = mapFragment.awaitMap()
        googleMap.awaitMapLoad()
        if (isSystemDarkMode(requireContext()))
            googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_style
                )
            )
        val bounds = LatLngBounds.builder()
        /*        response.data.forEach {
                    bounds.include(it.position)
        }*/
        bounds.include(LatLng(29.760836, 52.424100))
        bounds.include(LatLng(29.504610, 52.633141))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 40))
        files = response.data
        addClusteredMarkers(googleMap, filterFileByChosenType(files))
    }

    private fun filterFileByChosenType(files: List<LocationData>): List<LocationData> {
        return when (viewModel.getItemType()) {
            MainViewModel.ItemType.SHOW_ALL ->
                files

            MainViewModel.ItemType.SHOW_SEEKER ->
                files.filter { it.fileTypeId == FileTypes().seeker.id }

            MainViewModel.ItemType.SHOW_OWNER ->
                files.filter { it.fileTypeId == FileTypes().owner.id }
        }
    }

    private fun setTitleColor() {
        when (viewModel.getItemType()) {
            MainViewModel.ItemType.SHOW_OWNER -> binding.cardHeader.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.main_dark_color2)
            )

            MainViewModel.ItemType.SHOW_SEEKER -> binding.cardHeader.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.main_green_color2)
            )

            else -> binding.cardHeader.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.empty_background_color)
            )
        }
    }

    // This is for hide and unhiding bottom nav bar
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Interaction) {
            interaction = context as Interaction
        } else {
            throw RuntimeException(
                context.toString() + " must implement OnFragmentInteractionListener"
            )
        }
    }
    // This is for hide and unhiding bottom nav bar
    override fun onDetach() {
        super.onDetach()
        interaction = null
    }

    private fun onPolygonBtnChecked(){
        if (::googleMap.isInitialized)
            googleMap.setOnMapClickListener { latLng ->
                val markerOptions = MarkerOptions().position(latLng)
                val marker = googleMap.addMarker(markerOptions)
                markerList.add(marker!!)
                latLngList.add(latLng)
                if (::polygon.isInitialized)
                    polygon.remove()
                polygonOptions =
                    PolygonOptions()
                        .strokeColor(Color.rgb(35, 59, 136))
                        .strokeWidth(3f)
                        .fillColor(Color.argb(60, 193, 15, 65))
                        .addAll(latLngList)
                        .clickable(true)
                polygon = googleMap.addPolygon(polygonOptions)
            }
    }
    private fun onPolygonBtnUnChecked() {
        if (::polygon.isInitialized) {
            polygon.remove()
            markerList.forEach { it.remove() }
            latLngList.clear()
            markerList.clear()
            googleMap.setOnMapClickListener {}
        }
    }

    private fun readyViewsOnPolygonClicked() {
        if (isPolygonClicked) {
            onClusterMarkerClickOnAndOff(false)
            binding.ibtnDraw.setBackgroundResource(R.drawable.background_rounded_btns_sharp)
            binding.ibtnSelectDraw.visibility = View.VISIBLE
        } else {
            onClusterMarkerClickOnAndOff(true)
            binding.ibtnDraw.setBackgroundResource(R.drawable.background_rounded_btns)
            binding.ibtnSelectDraw.visibility = View.GONE
            addClusteredMarkers(googleMap, files)
        }
    }

    private fun isShowMarkerDialog(): Boolean {
        if (markerList.size < 3) {
            showDialogWithMessage(
                requireContext(),
                resources.getString(R.string.create_more_marker_to_proceed)
            ) { d, _ ->
                d.dismiss()
            }
            return true
        }
        return false
    }

    fun onMoreDetailFileClick() {
        findNavController().navigate(R.id.action_navigation_map_to_fileDetailFrag)
        interaction?.changBottomNavViewVisibility(View.GONE)
    }

    /************************ binding methods *********************************/
    fun showStatusTitle(): String {
        return when (viewModel.getItemType()) {
            MainViewModel.ItemType.SHOW_OWNER -> resources.getString(R.string.show_owner_files)
            MainViewModel.ItemType.SHOW_SEEKER -> resources.getString(R.string.show_seeker_files)
            else -> resources.getString(R.string.show_all_files)
        }
    }

    fun onChangeItemType() {
        val bottomFrag = BottomSheetUniversalList(
            resources.getStringArray(R.array.type_options).toList()
        )
        bottomFrag.show(childFragmentManager, TYPE_OPTIONS_TAG)
        bottomFrag.setFragmentResultListener(TYPE_OPTIONS_TAG) { _, bundle ->
            when (bundle.getInt(DATA)) {
                0 -> viewModel.setItemType(MainViewModel.ItemType.SHOW_ALL)
                1 -> viewModel.setItemType(MainViewModel.ItemType.SHOW_SEEKER)
                2 -> viewModel.setItemType(MainViewModel.ItemType.SHOW_OWNER)
            }
            binding.txtTypeOptionTitle.text = showStatusTitle()
            setTitleColor()
            if (::googleMap.isInitialized) addClusteredMarkers(
                googleMap,
                filterFileByChosenType(files)
            )
        }
    }

    fun onPolygonClick() {
        isPolygonClicked = !isPolygonClicked
        readyViewsOnPolygonClicked()
        if (isPolygonClicked)
            onPolygonBtnChecked()
        else
            onPolygonBtnUnChecked()
    }

    fun onChooseClickAfterSelectingPolygon() {
        if (isShowMarkerDialog()) return
        val polygonList = arrayListOf<LocationData>()
        clusterManager.clearItems()
        files.forEach {
            it.locations?.apply {
                if (polygon.contains(LatLng(it.locations[0].lat!!, it.locations[0].lng!!)))
                    polygonList.add(it)
            }
        }
        addClusteredMarkers(googleMap, polygonList)
        onClusterMarkerClickOnAndOff(true)
    }

    fun onSatelliteClicked(googleMap: GoogleMap) {
        if (googleMap.mapType == GoogleMap.MAP_TYPE_NORMAL) googleMap.mapType =
            GoogleMap.MAP_TYPE_SATELLITE
        else googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    }

    fun onFilterClicked() {
        findNavController().navigate(R.id.action_navigation_map_to_filterFilesFrag)
        interaction?.changBottomNavViewVisibility(View.GONE)
    }

}