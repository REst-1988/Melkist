package com.example.melkist.views.profile.cooperations

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.melkist.MainActivity
import com.example.melkist.adapters.SendReceivedCooperationAdapter
import com.example.melkist.databinding.FragProfileSendRecievedCooperationBinding
import com.example.melkist.utils.RECEIVED
import com.example.melkist.utils.SENT
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.MainViewModel

class ProfileReceivedCooperationFrag : Fragment() {

    private lateinit var binding: FragProfileSendRecievedCooperationBinding
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragProfileSendRecievedCooperationBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        initListeners()
    }

    private fun initListeners() {
        binding.pullToRefreshMainList.setOnRefreshListener {
            setupReceivedCooperation()
        }
    }
    private fun setupRecyclerView() {
        setupReceivedCooperation()
        binding.rvSendReceived.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
    }

    private fun setupReceivedCooperation() {
        val adapter = SendReceivedCooperationAdapter(requireContext(), RECEIVED)
        binding.rvSendReceived.adapter = adapter
        (activity as MainActivity).user?.apply {
            viewModel.getReceivedCooperation(
                id!!,
                token!!
            )
        }
        viewModel.sendReceiveCooperationResponse.observe(viewLifecycleOwner) {
            when (it.result) {
                true -> adapter.submitList(it.data)
                false -> showToast(requireContext(), concatenateText(it.errors))
                else -> {
                    Log.e("TAG", "setupOutbox: ${it.errors}")
                }
            }
            binding.pullToRefreshMainList.isRefreshing = false
        }
    }
}