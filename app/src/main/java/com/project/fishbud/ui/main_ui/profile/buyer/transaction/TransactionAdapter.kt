package com.project.fishbud.ui.main_ui.profile.buyer.transaction

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.fishbud.R
import com.project.fishbud.databinding.ItemTransactionBinding
import com.project.fishbud.databinding.ItemWaitingPaymentBinding
import com.project.fishbud.ui.main_ui.marketplace.checkout.PaymentEntity
import com.project.fishbud.ui.main_ui.profile.OrderFishermanEntity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TransactionAdapter(val mContext: Context) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    private var dataPembayaran = ArrayList<OrderFishermanEntity>()

    fun setdataPembayaran(data: List<OrderFishermanEntity>?) {
        if (data == null) return
        this.dataPembayaran.clear()
        this.dataPembayaran.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemTransactionBinding =
            ItemTransactionBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ViewHolder(itemTransactionBinding, mContext)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataPembayaran[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = dataPembayaran.size


    class ViewHolder(private val binding: ItemTransactionBinding, val mContext: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: OrderFishermanEntity) {
            with(binding) {
                invoice.text = itemView.resources.getString(R.string.invoice, data.idPembayaran)
                produkName.text = data.namaIkan
                fromStore.text = itemView.resources.getString(R.string.fromStore, data.tokoIkan)
                totalPayment.text = itemView.resources.getString(R.string.total_payment, formatRupiah(data.harga))
            }
        }

        private fun formatRupiah(number: Long): String {
            val localeID = Locale("IND", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            val formatRupiah = numberFormat.format(number)
            val split = formatRupiah.split(",")
            val length = split[0].length
            return split[0].substring(0, 2) + split[0].substring(2, length)
        }
    }
}