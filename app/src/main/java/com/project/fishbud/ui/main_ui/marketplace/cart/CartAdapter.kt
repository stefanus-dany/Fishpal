package com.project.fishbud.ui.main_ui.marketplace.cart

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.fishbud.databinding.ItemCartBinding
import com.project.fishbud.ui.main_ui.marketplace.IkanEntity
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CartAdapter(private var mCallback: CartHarga) : RecyclerView.Adapter<CartAdapter.CourseViewHolder>() {
    private var dataCart = ArrayList<IkanEntity>()

    fun setdataCart(data: List<IkanEntity>?) {
        if (data == null) return
        this.dataCart.clear()
        this.dataCart.addAll(data)
        this.dataCart.distinct()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val itemCartBinding =
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CourseViewHolder(itemCartBinding, parent.context, mCallback)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val data = dataCart[position]
        holder.bind(data, position)
    }

    override fun getItemCount(): Int = dataCart.size


    class CourseViewHolder(
        private val binding: ItemCartBinding,
        val mContext: Context,
        private val mCallback: CartHarga
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private var totalHarga = 0L

        fun bind(data: IkanEntity, position: Int) {
            with(binding) {
                namaIkan.text = data.namaIkan
                hargaKg.text = formatRupiah(data.harga, 1)
                tokoIkan.text = data.tokoIkan
                Glide.with(itemView.context)
                    .load(data.linkImage)
                    .into(ivIkan)

                minusQuantity.setOnClickListener {
                    if (countQuantity.text.toString() != "0") {
                        val numb = countQuantity.text.toString().toInt() - 1
                        countQuantity.setText(numb.toString())
                        hargaIkan.text = formatRupiah((data.harga * numb), 0)
                        val price = (data.harga * numb)

                        val tmp = formatRupiah(totalHarga, 0)
                        Log.i("cek_total", "totalHarga paling fix : $tmp")
                        //interface
                        mCallback.totalHarga(price, position, countQuantity.text.toString().toLong())
                        Log.i("cek_total", "totalHarga: $totalHarga")
                    }
                }

                plusQuantity.setOnClickListener {
                    val numb = countQuantity.text.toString().toInt() + 1
                    if ((data.harga * numb) < Int.MAX_VALUE && (data.harga * numb) > 0) {
                        countQuantity.setText(numb.toString())
                        hargaIkan.text = formatRupiah((data.harga * numb), 0)
                        val price = (data.harga * numb)
                        Log.i("cek_total", "totalHarga: $totalHarga")
                        //interface
                        mCallback.totalHarga(price, position, countQuantity.text.toString().toLong())
                    } else {
                        Toast.makeText(mContext, "Maksimum harga", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        //kg = 0 -> return without /kg
        //kg = 1 -> return with /kg
        private fun formatRupiah(number: Long, kg: Int): String {
            val localeID = Locale("IND", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            val formatRupiah = numberFormat.format(number)
            val split = formatRupiah.split(",")
            val length = split[0].length

            return if (kg == 0) {
                split[0].substring(0, 2) + split[0].substring(2, length)
            } else {
                split[0].substring(0, 2) + split[0].substring(2, length) + "/kg"
            }
        }
    }

    interface CartHarga {
        fun totalHarga(value: Long, position: Int, countQuantity : Long)
    }
}