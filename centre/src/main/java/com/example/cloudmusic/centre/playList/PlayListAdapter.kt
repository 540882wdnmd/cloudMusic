package com.example.cloudmusic.centre.playList

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloudmusic.centre.R
import com.example.cloudmusic.centre.databinding.ItemPlayListBinding
import com.example.cloudmusic.utils.service.MusicServiceOnBind
import com.example.cloudmusic.utils.webs.bean.response.Artist
import com.example.cloudmusic.utils.webs.bean.response.Song

class PlayListAdapter(beanData : List<Song>?) : RecyclerView.Adapter<PlayListAdapter.ViewHolder>() {

    private var onItemClickListener : ((Int)->Unit)? =null
    fun setOnClickListener(listener:((Int)->Unit)){
        onItemClickListener = listener
    }
    private val mData = ArrayList<Song>()
    init {
        if (beanData != null) {
            for (song in beanData){
                mData.add(song)
            }
        }
    }
    private lateinit var binidng : ItemPlayListBinding

    inner class ViewHolder(binding:ItemPlayListBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    listener -> listener(adapterPosition)
                }
            }
        }
        val songPic = binding.songPic
        val songName = binding.songName
        val songArtist = binding.songArtist


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binidng = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_play_list,parent,false)
        val holder = ViewHolder(binidng)
        holder.itemView.setOnClickListener {

        }
        return holder
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = mData[position]
        Glide.with(holder.itemView)
            .load(song.al.picUrl)
            .into(holder.songPic)

        holder.songName.text = song.name
        holder.songArtist.text = songArtistString(song.ar)

    }

    fun updateData(newData : List<Song>){
        mData.clear()
        for (song in newData){
            mData.add(song)
        }
        notifyDataSetChanged()
    }

    private fun songArtistString(arList : List<Artist>) : String{
        val builder : StringBuilder = StringBuilder()
        for (ar in arList){
            builder.append(ar.name).append("/")
        }
        return builder.toString()
    }

}