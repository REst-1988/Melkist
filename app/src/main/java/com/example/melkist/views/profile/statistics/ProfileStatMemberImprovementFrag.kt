package com.example.melkist.views.profile.statistics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragProfileStatMemberImprovementBinding
import com.example.melkist.databinding.LayoutChartItemMemberImprovementBinding
import com.example.melkist.models.ChartData
import com.example.melkist.models.FileActionsData
import com.example.melkist.utils.SparklineStyle
import com.example.melkist.utils.onRequestFalseResult
import com.example.melkist.viewmodels.MainViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class ProfileStatMemberImprovementFrag : Fragment() {

    private lateinit var binding: FragProfileStatMemberImprovementBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragProfileStatMemberImprovementBinding.inflate(layoutInflater)
        viewModel.getUserSubsetActionStat(requireActivity())
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ProfileStatMemberImprovementFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.subsetUsersActionsResponse.observe(viewLifecycleOwner) { response ->
            when (response.result){
                true -> handleMemberImproResponse(response.data)
                false -> onRequestFalseResult(
                    requireActivity(),
                    response.errors ?: listOf(resources.getString(R.string.empty))
                ) {}

                else -> {}
            }
        }
    }

    private fun handleMemberImproResponse(list: List<FileActionsData>?) {
        list?.apply {
            val dataList = viewModel.getStaffImproCount(list)
            binding.llStatStaffImpro.removeAllViewsInLayout()
            dataList.forEach{ userActionChart ->

                val actionLayout =
                    LayoutChartItemMemberImprovementBinding.inflate(LayoutInflater.from(requireContext()))
                actionLayout.txtName.text = userActionChart.userName
                setupLineChart(actionLayout, userActionChart.chartData)
                binding.llStatStaffImpro.addView(actionLayout.root)
            }
        }
    }

    private fun setupLineChart(binding: LayoutChartItemMemberImprovementBinding, activities: List<ChartData>) {
        val lineChart = binding.lineChart
        val yValues: MutableList<Entry> = ArrayList()
        val dates = arrayOfNulls<String>(activities.size)
        activities.forEachIndexed { i, memberImproData ->
            yValues.add(Entry(i.toFloat(), memberImproData.sum!!.toFloat()))
            dates[i] = memberImproData.date
        }
        val dataSet = LineDataSet(yValues, resources.getString(R.string.member_impro_chart_tag))
        val data = LineData(dataSet)
        val chartStyle = SparklineStyle(requireContext())
        chartStyle.styleChart(lineChart)
        chartStyle.styleLineDataSet(dataSet)
        lineChart.axisLeft.axisMaximum = 30f
        lineChart.axisLeft.axisMinimum = 0f
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(dates)
        lineChart.data = data
        lineChart.animateY(1000)
        lineChart.invalidate()
    }

    /****************** binding methods *******************/
    fun back() {
        Log.e("TAG", "back: ?? ")
        findNavController().popBackStack()
    }
}