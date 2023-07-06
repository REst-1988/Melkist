package com.example.melkist.views.profile.alerts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.melkist.MainActivity
import com.example.melkist.adapters.InboxOutboxAdapter
import com.example.melkist.databinding.FragProfileInBoxOutBoxBinding
import com.example.melkist.utils.INBOX
import com.example.melkist.utils.OUTBOX
import com.example.melkist.viewmodels.MainViewModel

class ProfileInboxOutboxFrag(
    private val frag: Int
) : Fragment() {

    private lateinit var binding: FragProfileInBoxOutBoxBinding
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragProfileInBoxOutBoxBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        listenToInboxRequest()
        listenToOutboxRequest()
    }

    private fun listenToInboxRequest() {

    }

    private fun listenToOutboxRequest() {

    }

    private fun setupRecyclerView() {
        if (frag == INBOX)
            setupInbox()
        else
            setupOutbox()
    }

    private fun setupInbox() {
        val adapter = InboxOutboxAdapter(requireContext(), INBOX)
        binding.rvInboxOutbox.adapter = adapter
        (activity as MainActivity).user?.apply {
            viewModel.getInbox(
                id!!,
                token!!
            )
        }
        viewModel.inboxResponse.observe(viewLifecycleOwner) {
            adapter.submitList(it.data)
        }
    }

    private fun setupOutbox() {
        val adapter2 = InboxOutboxAdapter(requireContext(), OUTBOX)
        binding.rvInboxOutbox.adapter = adapter2
        (activity as MainActivity).user?.apply {
            viewModel.getOutbox(
                id!!,
                token!!
            )
        }
        viewModel.outboxResponse.observe(viewLifecycleOwner) {
            when (it.result){
                true -> adapter2.submitList(it.data)
                else -> {
                    Log.e("TAG", "setupOutbox: ${it.errors}", )}
            }

        }
    }
}