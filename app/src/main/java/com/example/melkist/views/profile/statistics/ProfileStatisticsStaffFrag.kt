package com.example.melkist.views.profile.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragProfileStatisticsStaffBinding
import com.example.melkist.databinding.LayoutItemListStatStaffBinding
import com.example.melkist.models.FileActionsData
import com.example.melkist.utils.isSystemThemeInDarkMode
import com.example.melkist.utils.onRequestFalseResult
import com.example.melkist.viewmodels.MainViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.floor

class ProfileStatisticsStaffFrag : Fragment() {

    private lateinit var binding: FragProfileStatisticsStaffBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragProfileStatisticsStaffBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@ProfileStatisticsStaffFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserSubsetActionStat(requireActivity())
    }

    private fun initObservers() {
        viewModel.subsetUsersActionsResponse.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> showStaffStat(response.data)
                false -> {
                    onRequestFalseResult(
                        requireActivity(),
                        response.errors ?: listOf(resources.getString(R.string.empty))
                    ) {}
                    viewModel.resetSubsetUsersActionsResponse()
                }

                else -> {}
            }
        }
    }

    private fun showStaffStat(fileActionsData: List<FileActionsData>?) {
        binding.llStatStaff.removeAllViewsInLayout()
        val calculationResult = viewModel.getStatStaffCount(fileActionsData)
        setupMostVisitBarChart(calculationResult)
        calculationResult.forEachIndexed { index, userActionStat ->
            val actionLayout =
                LayoutItemListStatStaffBinding.inflate(LayoutInflater.from(requireContext()))
            actionLayout.txtGrade.text = (index.inc()).toString()
            actionLayout.txtName.text = userActionStat[1]
            actionLayout.txtFileCreation.text = userActionStat[2]
            actionLayout.txtVisitCount.text = userActionStat[3]
            actionLayout.txtMeetingCount.text = userActionStat[4]
            actionLayout.txtContractCount.text = userActionStat[5]
            actionLayout.txtConsultCount.text = userActionStat[6]
            actionLayout.txtSum.text = userActionStat[7]
            binding.llStatStaff.addView(actionLayout.root)
        }
    }

    private fun setupMostVisitBarChart(data: List<Array<String>>) {
        val barChart = binding.barChart
        val barEntryList: MutableList<BarEntry> = ArrayList()
        val names = arrayOfNulls<String>(data.size)
        data.forEachIndexed { index, strings ->
            barEntryList.add(BarEntry(index.toFloat(), strings[7].toFloat()))
            names[index] = strings[1]
        }
        val dataSet = BarDataSet(barEntryList, resources.getString(R.string.member_impro_chart_tag))
        dataSet.setColors(Color.BLUE)
        //dataSet.set;
        val data = BarData(dataSet)
        data.barWidth = 0.7f
        data.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return floor(value.toDouble()).toInt().toString()
            }
        })
        /*XAxis xAxis = mostVisitBarChart.getXAxis();
        //xAxis.setValueFormatter();
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(1);*/
        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = if (isSystemThemeInDarkMode(requireContext()))
            Color.rgb(203, 172, 123)
        else
            Color.rgb(13, 23, 64)
        //xAxis.setGranularity(1f);
        //xAxis.setAxisMinimum(1);
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(names)
        barChart.xAxis.labelCount = names.size
        barChart.setVisibleXRangeMaximum(names.size.toFloat())
        barChart.xAxis.setCenterAxisLabels(false)
        xAxis.setAvoidFirstLastClipping(false)
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.isDragXEnabled = true
        barChart.setFitBars(true)
        barChart.setPinchZoom(false)
        barChart.axisRight.isEnabled = false
        barChart.axisLeft.isEnabled = false
        barChart.axisRight.setDrawLabels(false)
        barChart.axisLeft.setDrawLabels(false)
        barChart.xAxis.labelRotationAngle = -90f
        //mostVisitBarChart.setDrawGridBackground(true);
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.setDrawGridLines(false)
        barChart.xAxis.setDrawGridLines(false)
        barChart.data = data
        barChart.animateY(1000)
        barChart.invalidate()
    }

    /**************** binding methods *******************/
    fun back() {
        findNavController().popBackStack()
    }


}