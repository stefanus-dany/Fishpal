package com.project.fishbud.ui.main_ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.fishbud.databinding.ItemIkanHomeBinding
import com.project.fishbud.ui.main_ui.marketplace.IkanEntity
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class IkanHomeAdapter(mContext: Context) :
    RecyclerView.Adapter<IkanHomeAdapter.CourseViewHolder>() {
    private var dataIkan = ArrayList<IkanEntity>()

    fun setdataIkan(data: List<IkanEntity>?) {
        if (data == null) return
        this.dataIkan.clear()
        this.dataIkan.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val itemIkanHomeBinding =
            ItemIkanHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(itemIkanHomeBinding, parent.context)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val data = dataIkan[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = dataIkan.size


    class CourseViewHolder(private val binding: ItemIkanHomeBinding, val mContext: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: IkanEntity) {
            with(binding) {
                namaIkan.text = data.namaIkan
                hargaIkan.text = formatRupiah(data.harga)
                tokoIkan.text = data.tokoIkan
                Glide.with(itemView.context)
                    .load(data.linkImage)
                    .into(ivIkan)
            }
        }

        private fun formatRupiah(number: Long): String {
            val localeID = Locale("IND", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            val formatRupiah = numberFormat.format(number)
            val split = formatRupiah.split(",")
            val length = split[0].length
            return split[0].substring(0, 2) + split[0].substring(2, length) + "/kg"
        }
    }


}