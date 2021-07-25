package com.karyaprestasi.fishpal.ui.main_ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.karyaprestasi.fishpal.databinding.ItemCommunityHomeBinding
import com.karyaprestasi.fishpal.ui.main_ui.community.CommunityEntity

class CommunityHomeAdapter(val mContext: Context) :
    RecyclerView.Adapter<CommunityHomeAdapter.CourseViewHolder>() {
    private var dataThread = ArrayList<CommunityEntity>()

    fun setdataThread(data: List<CommunityEntity>?) {
        if (data == null) return
        this.dataThread.clear()
        this.dataThread.addAll(data)
        Log.i("cekIkan", "data di adapter : ${this.dataThread}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val itemCommunityHome =
            ItemCommunityHomeBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CourseViewHolder(itemCommunityHome, mContext)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val data = dataThread[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = dataThread.size

    class CourseViewHolder(
        private val binding: ItemCommunityHomeBinding,
        val mContext: Context,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CommunityEntity) {
            with(binding) {
                userNameThread.text = data.writerName
                Glide.with(mContext)
                    .load(data.urlProfileImage)
                    .into(civUser)
                descThread.text = data.threadDesc
            }
            Log.i("cekGetData", "cek Get Data :$data ")
        }

    }
}