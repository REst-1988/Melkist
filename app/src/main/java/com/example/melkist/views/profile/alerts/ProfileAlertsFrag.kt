package com.example.melkist.views.profile.alerts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.adapters.ProfileAlertsViewPagerAdapter
import com.example.melkist.databinding.FragProfileAlertsBinding
import com.example.melkist.utils.INBOX
import com.example.melkist.utils.OUTBOX
import com.google.android.material.tabs.TabLayoutMediator


class ProfileAlertsFrag : Fragment() {

    private lateinit var binding: FragProfileAlertsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragProfileAlertsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ibtnBack.setOnClickListener { back() }
        setupTabLayoutAndPagerLayout()
    }

    private fun setupTabLayoutAndPagerLayout() {
        val adapter = ProfileAlertsViewPagerAdapter(requireActivity())
        adapter.addFragment(ProfileInboxOutboxFrag(INBOX), resources.getString(R.string.in_box))
        adapter.addFragment(ProfileInboxOutboxFrag(OUTBOX), resources.getString(R.string.out_box))
        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = INBOX
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

    fun back() {
        findNavController().popBackStack()
    }
}