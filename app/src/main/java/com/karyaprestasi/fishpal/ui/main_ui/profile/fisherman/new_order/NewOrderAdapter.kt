package com.karyaprestasi.fishpal.ui.main_ui.profile.fisherman.new_order

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.karyaprestasi.fishpal.R
import com.karyaprestasi.fishpal.databinding.ItemNewOrderBinding
import com.karyaprestasi.fishpal.ui.main_ui.profile.OrderFishermanEntity
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class NewOrderAdapter(val mContext: Context, var mCallback: dataNewOrder) :
    RecyclerView.Adapter<NewOrderAdapter.ViewHolder>() {
    private var dataPembayaran = ArrayList<OrderFishermanEntity>()
    private var idPesanan = ArrayList<String>()

    fun setdataPembayaran(data: List<OrderFishermanEntity>?) {
        if (data == null) return
        this.dataPembayaran.clear()
        this.dataPembayaran.addAll(data)
        Log.i("cekut", "dataPembayaran: ${dataPembayaran.size}")

//        this.idPesanan.clear()
//        this.idPesanan.addAll(idPesanan)
//        Log.i("cekut", "idPesanan: ${idPesanan.size}")
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemNewOrderBinding =
            ItemNewOrderBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ViewHolder(itemNewOrderBinding)
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataPembayaran[position]
        val getItemCount = dataPembayaran.size
//        val idPesanan = idPesanan[position]
//        holder.bind(data,  position, getItemCount)
        with(holder.binding) {
            invoice.text = holder.itemView.resources.getString(R.string.invoice, data.idPesanan)
            produkName.text = data.namaIkan
            total.text =
                holder.itemView.resources.getString(R.string.total_new_order, formatRupiah(data.harga, 0))
            val tmp = (data.harga * 0.05).toLong()
            adminCost.text = "Biaya Admin (5%) : ${formatRupiah(tmp, 0)}"
            pendapatan.text =
                holder.itemView.resources.getString(R.string.pendapatan, formatRupiah((data.harga - tmp), 0))

            btnProsesSekarang.setOnClickListener {
                val value = OrderFishermanEntity(
                    data.nelayanId,
                    data.idPesanan, //ini idproduk
                    data.namaIkan,
                    data.harga,
                    data.totalHarga,
                    data.linkImage,
                    data.tokoIkan,
                    data.idPembayaran,
                    data.idBuyer
                )

                mCallback.getDataNewOrder(value, position, getItemCount)
                notifyItemRemoved(position)
            }


        }
    }

    override fun getItemCount(): Int = dataPembayaran.size

    class ViewHolder(
        val binding: ItemNewOrderBinding
    ) :
        RecyclerView.ViewHolder(binding.root)

    interface dataNewOrder {
        fun getDataNewOrder(data: OrderFishermanEntity, position: Int, getItemCount : Int)
    }
}