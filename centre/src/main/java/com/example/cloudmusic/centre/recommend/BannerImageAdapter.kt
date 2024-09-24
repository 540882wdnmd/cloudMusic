package com.example.cloudmusic.centre.recommend


import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloudmusic.utils.webs.bean.data.BannerImage
import com.youth.banner.adapter.BannerAdapter


class BannerImageAdapter(beanData: List<BannerImage>?) :
    BannerAdapter<BannerImage, BannerImageAdapter.BannerViewHolder>(beanData) {

    inner class BannerViewHolder(view: ImageView) : RecyclerView.ViewHolder(view) {
        val imageView = view
    }

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent?.context)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerViewHolder(imageView)
    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: BannerImage?,
        position: Int,
        size: Int
    ) {
        if (holder != null) {
            if (data != null) {
                Glide.with(holder.itemView)
                    .load(data.imageUrl)
                    .into(holder.imageView)
            }
        }
    }
}