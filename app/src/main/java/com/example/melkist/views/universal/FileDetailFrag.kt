package com.example.melkist.views.universal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.ImagePagerAdapter
import com.example.melkist.databinding.FragFileDetailBinding
import com.example.melkist.models.FileDataResponse
import com.example.melkist.models.Location
import com.example.melkist.models.Period
import com.example.melkist.utils.formatNumber
import com.example.melkist.viewmodels.MainViewModel

class FileDetailFrag : Fragment() {

    private lateinit var binding: FragFileDetailBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var data: FileDataResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = viewModel.fileAllData.value!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragFileDetailBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@FileDetailFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = ImagePagerAdapter(null, data)
        binding.indicator.setViewPager(binding.viewPager)
        listenToCooperationResponse()
    }

    private fun listenToCooperationResponse() {
        viewModel.publicResponse.observe(viewLifecycleOwner){
            Log.e("TAG", "listenToCooperationResponse: ${it.toString()}", )
        }
    }

    private fun showOtherRegions(locations: List<Location>): String {
        var text = ""
        if (locations.size > 1) {
            binding.txtRegionOther.visibility = View.VISIBLE
            for (i in 1 until locations.size) text += locations[i].region.title
        } else
            binding.txtRegionOther.visibility = View.GONE
        return text
    }

    private fun getPropertyPeriodsText(period: Period): String {
        val conjunctions = if (period.to == period.from)
            ""
        else
            resources.getString(R.string.to) + " " + period.to
        return String.format("%s %s", period.from, conjunctions)
    }

    private fun calculatePricePerMeter(price: Period, size: Period): String {
        val priceTo = price.to ?: 0
        val priceFrom = price.from ?: 0
        val sizeTo = size.to ?: 1
        val sizeFrom = size.from ?: 1
        return if (price.to == price.from)
            (priceFrom / sizeFrom).toString()
        else
            (priceFrom / sizeFrom).toString() + " " + resources.getString(R.string.to) + " " + (priceTo / sizeTo)
    }

    /************** binding methods ************************/
    fun back() {
        findNavController().popBackStack()
    }

    fun onSendCooperationRequest() {
        (activity as MainActivity).user?.apply {
            viewModel.sendCooperationRequest( // TODO: finish this part
                token!!,
                id!!,
                data.data!!.id
            )
        }
    }

    fun getUserImage(): String? {
        return data.data?.user?.profilePic
    }

    fun advertiserText(): String {
        return String.format("%s %s", data.data!!.user.firstName, data.data!!.user.lastName)
    }

    fun realEstateText(): String {
        return data.data!!.user.realEstate?: resources.getString(R.string.freelancer)
    }

    fun reginText(): String {
        return data.data!!.locations[0].region.title
    }

    fun otherRegionText(): String {
        return showOtherRegions(data.data!!.locations)
    }

    fun isShowOtherRegion(): Boolean {
        return true // TODO: check this replace with right method
    }

    ////////////////////////////////////////
    fun ageText(): String {
        return getPropertyPeriodsText(data.data!!.age)
    }

    fun sizeText(): String {
        return String.format(
            "%s %s",
            getPropertyPeriodsText(data.data!!.size),
            resources.getString(R.string.squere_meter)
        )
    }

    fun roomNoText(): String {
        return getPropertyPeriodsText(data.data!!.roomNo)
    }

    fun priceText(): String {
        return String.format(
            "%s %s",
            formatNumber(getPropertyPeriodsText(data.data!!.price).toDouble()),
            resources.getString(R.string.tooman)
        )
    }

    fun pricePerMeterText(): String {
        return String.format(
            "%s %s",
            formatNumber(calculatePricePerMeter(data.data!!.price, data.data!!.size).toDouble()),
            resources.getString(R.string.tooman)
        )
    }

    fun isShowPricePerMeter(): Boolean {
        return true // TODO: check this replace with right method (proper item in request amir)
    }

    fun descriptionsText(): String {
        return data.data!!.description ?: ""
    }
}