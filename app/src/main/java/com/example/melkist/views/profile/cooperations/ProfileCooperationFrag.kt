package com.example.melkist.views.profile.cooperations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.adapters.ProfileAlertsViewPagerAdapter
import com.example.melkist.databinding.FragProfileCooperationsBinding
import com.example.melkist.utils.INBOX
import com.example.melkist.utils.RECEIVED
import com.example.melkist.utils.SENT
import com.google.android.material.tabs.TabLayoutMediator


class ProfileCooperationFrag : Fragment() {

    private lateinit var binding: FragProfileCooperationsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragProfileCooperationsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ibtnBack.setOnClickListener { back() }
        setupTabLayoutAndPagerLayout()
    }

    private fun setupTabLayoutAndPagerLayout() {
        val adapter = ProfileAlertsViewPagerAdapter(requireActivity())
        adapter.addFragment(ProfileReceivedCooperationFrag(this), resources.getString(R.string.recieved))
        adapter.addFragment(ProfileSendCooperationFrag(this), resources.getString(R.string.sent))
        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = INBOX
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

    fun back() {
        findNavController().popBackStack()
    }

    fun navigateToDetail() {
        findNavController().navigate(
            R.id.action_profileCooperationFrag_to_fileDetailFrag
        )
    }
}