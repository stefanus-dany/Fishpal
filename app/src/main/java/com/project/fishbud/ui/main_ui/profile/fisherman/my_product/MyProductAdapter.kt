package com.project.fishbud.ui.main_ui.profile.fisherman.my_product

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.fishbud.R
import com.project.fishbud.databinding.ItemIkanBinding
import com.project.fishbud.databinding.ItemMyProductBinding
import com.project.fishbud.ui.main_ui.marketplace.IkanEntity
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class MyProductAdapter(val mContext: Context) :
    RecyclerView.Adapter<MyProductAdapter.CourseViewHolder>() {
    private var dataIkan = ArrayList<IkanEntity>()

    fun setdataIkan(data: List<IkanEntity>?) {
        if (data == null) return
        this.dataIkan.clear()
        this.dataIkan.addAll(data)
        Log.i("cekIkan", "data di adapter : ${this.dataIkan}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val itemMyProduct =
            ItemMyProductBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CourseViewHolder(itemMyProduct, mContext)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val data = dataIkan[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = dataIkan.size

    class CourseViewHolder(private val binding: ItemMyProductBinding, val mContext: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: IkanEntity) {
            with(binding) {
                namaIkan.text = data.namaIkan
                Log.i("cekIkan", "namaIkan : ${data.namaIkan}")
                hargaIkan.text = formatRupiah(data.harga)
                Glide.with(mContext)
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