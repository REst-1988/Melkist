package com.example.melkist.views.profile.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragProfileStatWorkPeaksBinding
import com.example.melkist.models.ChartData
import com.example.melkist.utils.SparklineStyle
import com.example.melkist.utils.getCorrectDate
import com.example.melkist.utils.getMonthNameWithMonthNo
import com.example.melkist.utils.getPersianMonthNumber
import com.example.melkist.utils.onRequestFalseResult
import com.example.melkist.viewmodels.MainViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class ProfileStatWorkPeaksFrag : Fragment() {

    private lateinit var binding: FragProfileStatWorkPeaksBinding
    private val viewModel: MainViewModel by activityViewModels()
    lateinit var chartStyle: SparklineStyle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getMemberImproChart(requireActivity(), 6)
        binding = FragProfileStatWorkPeaksBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ProfileStatWorkPeaksFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.memberImproChartResponse.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> {
                    handleWorkPeakResponse(response.data)
                    viewModel.resetWorkPeakResponse()
                }

                false -> {
                    onRequestFalseResult(
                        requireActivity(),
                        response.errors ?: listOf(resources.getString(R.string.empty))
                    ) {}
                    viewModel.resetWorkPeakResponse()
                }

                else -> {}
            }
        }
    }

    private fun handleWorkPeakResponse(response: List<ChartData>?) {
        response?.apply {
            setupDailyVisitLineChart(this)
        /* //  delete this part
            val a =  mutableListOf<ChartData>()
            response.forEach {
                val mem = ChartData(it.date, (0..8).random().toDouble())
                a.add(mem)
            }
            setupDailyVisitLineChart(a)*/
        }
    }

    private fun setupDailyVisitLineChart(activities: List<ChartData>) {
        val lineChart = binding.lineChart
        val yValues: MutableList<Entry> = ArrayList()
        val dates = arrayOfNulls<String>(activities.size)
        activities.forEachIndexed { i, memberImproData ->
            yValues.add(Entry(i.toFloat(), memberImproData.sum!!.toFloat()))
            dates[i] = getMonthNameWithMonthNo(
                getPersianMonthNumber(
                    getCorrectDate(memberImproData.date?.toLong() ?: 0)
                )
            )
        }
        val dataSet = LineDataSet(yValues, resources.getString(R.string.member_impro_chart_tag))
        val data = LineData(dataSet)
        chartStyle = SparklineStyle(requireContext())
        chartStyle.styleChart(lineChart)
        chartStyle.styleLineDataSet(dataSet)
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(dates)
        lineChart.data = data
        lineChart.animateY(1000)
        lineChart.invalidate()
    }

    /**************** binding methods ********************/
    fun back() {
        findNavController().popBackStack()
    }
}