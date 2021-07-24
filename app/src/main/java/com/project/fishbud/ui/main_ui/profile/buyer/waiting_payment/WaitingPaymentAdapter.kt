package com.project.fishbud.ui.main_ui.profile.buyer.waiting_payment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.fishbud.R
import com.project.fishbud.databinding.ItemWaitingPaymentBinding
import com.project.fishbud.ui.main_ui.marketplace.checkout.PaymentEntity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class WaitingPaymentAdapter(val mContext: Context, var mCallback: dataWaitingPayment) :
    RecyclerView.Adapter<WaitingPaymentAdapter.ViewHolder>() {
    private var dataPembayaran = ArrayList<PaymentEntity>()

    fun setdataPembayaran(data: List<PaymentEntity>?) {
        if (data == null) return
        this.dataPembayaran.clear()
        this.dataPembayaran.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemWaitingPayment =
            ItemWaitingPaymentBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ViewHolder(itemWaitingPayment)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataPembayaran[position]
//        holder.bind(data, position, dataPembayaran.size)
        with(holder.binding) {
            dateOrder.text = holder.itemView.resources.getString(com.project.fishbud.R.string.date_order, data.date)
            //date + 1 day
            var dt = data.timeDate // Start date
            val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", java.util.Locale.US)
            val c = java.util.Calendar.getInstance()
            c.time = sdf.parse(dt) as Date
            c.add(java.util.Calendar.DATE, 1) // number of days to add
            dt = sdf.format(c.time) // dt is now the new date

            payBefore.text = holder.itemView.resources.getString(com.project.fishbud.R.string.pay_before, dt)
            total.text = holder.itemView.resources.getString(
                com.project.fishbud.R.string.total_waiting_payment,
                formatRupiah(data.totalHarga)
            )
            idPayment.text =
                holder.itemView.resources.getString(com.project.fishbud.R.string.id_payment, data.idPembayaran)
            idCreditCard.text =
                holder.itemView.resources.getString(com.project.fishbud.R.string.no_credit_card, data.kartuKredit)

            btnPay.setOnClickListener {
                val value = WaitingPaymentEntity(
                    data.idPembayaran,
                    data.buyerName,
                    data.buyerId,
                    data.alamat,
                    data.kartuKredit,
                    data.jenisPengiriman,
                    data.totalHarga,
                    data.netPrice,
                    data.date,
                    dt
                )
                mCallback.getData(value, position, dataPembayaran.size)
                notifyItemRemoved(position)
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

    override fun getItemCount(): Int = dataPembayaran.size


    class ViewHolder(
        val binding: ItemWaitingPaymentBinding
    ) :
        RecyclerView.ViewHolder(binding.root)

    interface dataWaitingPayment {
        fun getData(data: WaitingPaymentEntity, position: Int, getitemCount : Int)
    }
}