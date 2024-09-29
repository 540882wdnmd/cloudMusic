package com.example.cloudmusic.centre.recommend

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cloudmusic.centre.databinding.FragmentRecommendBinding
import com.example.cloudmusic.utils.TAG
import com.example.cloudmusic.utils.toast

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
    ): View {
        _binding = FragmentRecommendBinding.inflate(inflater,container,false)
        val rootView = binding.root
        banner()
        return rootView
    }

    private fun banner(){
        val banner = binding.bannerRecommend
        bannerImageAdapter = BannerImageAdapter(null)
        banner.setAdapter(bannerImageAdapter)
        recommendViewModel.banner.observe(viewLifecycleOwner){
            if (it != null) {
                if (it.code==200){
                    bannerImageAdapter.updateBannerList(it.banners)
                    it.banners?.get(0)?.let { it1 -> Log.d(TAG, it1.pic) }
                }else if(it.code==400){
                    toast("获取Banner失败")
                }
            }else{
                toast("网络链接失败")
            }
        }
        recommendViewModel.getBanner()

    }

}