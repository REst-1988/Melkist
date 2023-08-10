package com.example.melkist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.melkist.databinding.ItemListSingleTextBinding
import com.example.melkist.views.universal.dialog.BottomSheetUniversalList

class BottomSheetUniversalAdapter (val list: List<String>, val fragment: BottomSheetUniversalList) :
    RecyclerView.Adapter<BottomSheetUniversalAdapter.BottomSheetUniversalViewHolder>() {

    class BottomSheetUniversalViewHolder(val binding: ItemListSingleTextBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String) {
            binding.txt.text = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetUniversalViewHolder {
        return BottomSheetUniversalViewHolder(
            ItemListSingleTextBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BottomSheetUniversalViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            fragment.setOnItemClickListener(position)
        }
    }
}