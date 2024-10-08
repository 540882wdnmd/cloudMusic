package com.example.cloudmusic.centre.recommend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloudmusic.centre.R
import com.example.cloudmusic.centre.databinding.ItemPersonalizedBinding
import com.example.cloudmusic.utils.webs.bean.response.PersonalizedResult

class PersonalizedAdapter(beanData: List<PersonalizedResult>?) : RecyclerView.Adapter<PersonalizedAdapter.PersonalizedViewHolder>() {

    private lateinit var binding : ItemPersonalizedBinding
    private  val mData = ArrayList<PersonalizedResult>()
    init {
            if (beanData != null) {
                for (result in beanData){
                    mData.add(result)
                }
            }
        }

    inner class PersonalizedViewHolder(binding: ItemPersonalizedBinding) : RecyclerView.ViewHolder(binding.root){
        val imageView = binding.personalizedImg
        val textView = binding.personalizedText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalizedViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_personalized,parent,false)
        return PersonalizedViewHolder(binding)
    }

    override fun getItemCount()= mData.size

    override fun onBindViewHolder(holder: PersonalizedViewHolder, position: Int) {
        val result = mData[position]
        Glide.with(holder.itemView)
            .load(result.picUrl)
            .into(holder.imageView)
        holder.textView.text = result.name
    }

    fun updateData(data : List<PersonalizedResult>?){
        if (data!=null){
            mData.clear()
            for (d in data){
                mData.add(d)
            }
        }
        notifyDataSetChanged()
    }
}