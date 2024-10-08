package com.example.cloudmusic.centre.recommend

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloudmusic.centre.databinding.FragmentRecommendBinding
import com.example.cloudmusic.centre.databinding.ItemPersonalizedBinding
import com.example.cloudmusic.utils.TAG
import com.example.cloudmusic.utils.toast

class RecommendFragment :Fragment() {

    private lateinit var bannerImageAdapter: BannerImageAdapter
    private lateinit var personalizedAdapter: PersonalizedAdapter

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
        personalized()
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
                }else if(it.code==400){
                    toast("获取Banner失败")
                }
            }else{
                toast("网络链接失败")
            }
        }
        recommendViewModel.getBanner()

    }

    private fun personalized(){
        val personalized = binding.personalizedRecommend
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        personalized.layoutManager = layoutManager
        personalizedAdapter = PersonalizedAdapter(null)
        personalized.adapter = personalizedAdapter
        recommendViewModel.personalized.observe(viewLifecycleOwner){
            if (it != null) {
                if (it.code==200){
                    personalizedAdapter.updateData(it.result)
                }else if(it.code==400){
                    toast("获取Banner失败")
                }
            }else{
                toast("网络链接失败")
            }
        }
        recommendViewModel.getPersonalized()
    }

}