package com.project.fishbud.ui.main_ui.marketplace

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
import com.bumptech.glide.request.RequestOptions
import com.project.fishbud.OnItemClick
import com.project.fishbud.R
import com.project.fishbud.databinding.ItemIkanBinding
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList


class IkanAdapter(mContext: Context, onItemClick: OnItemClick) : RecyclerView.Adapter<IkanAdapter.CourseViewHolder>() {
    private var dataIkan = ArrayList<IkanEntity>()
    private val mCallback = onItemClick

    fun setdataIkan(data: List<IkanEntity>?) {
        if (data == null) return
        this.dataIkan.clear()
        this.dataIkan.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val itemIkanBinding =
            ItemIkanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(itemIkanBinding, parent.context, mCallback)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val data = dataIkan[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = dataIkan.size


    class CourseViewHolder(private val binding: ItemIkanBinding, val mContext: Context, val mCallback : OnItemClick) :
        RecyclerView.ViewHolder(binding.root) {

        private var bgthread: Thread? = null

        fun bind(data: IkanEntity) {
            with(binding) {
                namaIkan.text = data.namaIkan
                hargaIkan.text = formatRupiah(data.harga)
                tokoIkan.text = data.tokoIkan
                Glide.with(itemView.context)
                    .load(data.linkImage)
                    .into(ivIkan)

                addToCart.setOnClickListener {
                    val dataIkanToCart = IkanEntity(
                        data.idProduk,
                        data.namaIkan,
                        data.harga,
                        data.tokoIkan,
                        data.linkImage,
                        data.userId
                    )
                    Log.i("cek_bind", "bind: $dataIkanToCart")
                    val executor = Executors.newSingleThreadExecutor()
                    val handler = Handler(Looper.getMainLooper())
                    executor.execute {
                        // Simulate process in background thread
                        try {
                            addToCart.background =
                                ContextCompat.getDrawable(mContext, R.drawable.bg_cart_click)
                            Thread.sleep(500) // in milisecond
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                        handler.post {
                            // Update ui in main thread
                            bgthread = Thread()
                            bgthread?.start()
                            addToCart.background =
                                ContextCompat.getDrawable(mContext, R.drawable.bg_cart)
                            Toast.makeText(mContext, "Ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
                        }
                    }
                    mCallback.onClick(dataIkanToCart)

                }
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