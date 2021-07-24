package com.project.fishbud.ui.main_ui.community
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
import com.project.fishbud.databinding.ItemCommunityBinding
import com.project.fishbud.databinding.ItemIkanBinding
import com.project.fishbud.databinding.ItemMyProductBinding
import com.project.fishbud.ui.main_ui.marketplace.IkanEntity
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class CommunityAdapter(val mContext: Context) :
    RecyclerView.Adapter<CommunityAdapter.CourseViewHolder>() {
    private var dataThread = ArrayList<CommunityEntity>()

    fun setdataThread(data: List<CommunityEntity>?) {
        if (data == null) return
        this.dataThread.clear()
        this.dataThread.addAll(data)
        Log.i("cekIkan", "data di adapter : ${this.dataThread}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val itemCommunity =
            ItemCommunityBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CourseViewHolder(itemCommunity, mContext)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val data = dataThread[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = dataThread.size

    class CourseViewHolder(private val binding: ItemCommunityBinding, val mContext: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CommunityEntity) {
            with(binding) {
                userNameThread.text = data.writerName
                dateTime.text = data.dateTime
                Glide.with(mContext)
                    .load(data.urlProfileImage)
                    .into(civUser)
                descThread.text = data.threadDesc
            }
        }
    }
}