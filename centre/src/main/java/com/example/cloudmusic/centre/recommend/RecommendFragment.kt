package com.example.cloudmusic.centre.recommend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cloudmusic.centre.databinding.FragmentRecommendBinding
import com.example.cloudmusic.utils.webs.bean.data.BannerImage
import com.example.cloudmusic.centre.recommend.BannerImageAdapter
import com.youth.banner.Banner

class RecommendFragment :Fragment() {

    private lateinit var bannerImageAdapter: BannerImageAdapter

    private val recommendViewModel by lazy { ViewModelProvider(this)[RecommendViewModel::class] }

    private var _binding : FragmentRecommendBinding ?= null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecommendBinding.inflate(inflater,container,false)
        val rootView = binding.root

        bannerImageAdapter = BannerImageAdapter(recommendViewModel.bannerImage.value)
//        val banner = Banner<BannerImage>
//        with(binding){
//            banner = bannerRecommend
//        }

        return rootView
    }

}